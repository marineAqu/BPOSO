package teamcom.comfirstpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamcom.comfirstpro.DTO.MemberDTO;
import teamcom.comfirstpro.DTO.MovieinfoDTO;
import teamcom.comfirstpro.principal.PrincipalDetails;
import teamcom.comfirstpro.principal.PrincipalDetailsService;
import teamcom.comfirstpro.service.MemberService;
import teamcom.comfirstpro.service.MovieinfoService;
import teamcom.comfirstpro.service.ReviewService;
import teamcom.comfirstpro.service.WantseeService;
import teamcom.comfirstpro.validator.SignUpFormValidator;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TempController {

    private final MemberService memberService;
    private final SignUpFormValidator signUpFormValidator;
    private final ReviewService reviewService;
    private final WantseeService wantseeService;
    private final PrincipalDetailsService principalDetailsService;
    private final MovieinfoService movieinfoService;

    @GetMapping("search-result")
    public String searchResult(@RequestParam("searchName") String searchName,
                               @RequestParam(value = "genre", required = false) List<String> genres,
                               @RequestParam("sort") String sort,
                               Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        if(userDetails != null) model.addAttribute("loginId", userDetails.getUsername());

        model.addAttribute("movieList", movieinfoService.SearchMovie(searchName, genres, sort));

        return "search-result";
    }


    @GetMapping("login")
    public String login(HttpServletRequest request, HttpSession session, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) return "main"; //이미 로그인했을 경우 메인으로


        session.setAttribute("lastPage", request.getHeader("Referer")); //로그인 성공 시 이전 페이지로 이동
        return "login";
    }


    //스프링 시큐리티 사용 이전 로그인
/*
    @PostMapping("login")
    public String loginFun(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        //Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginId", loginResult.getUsername());

            System.out.print(session.getAttribute("loginId"));
            return "main";
        } else {
            // login 실패
            System.out.print("login fail->"+memberDTO);
            return "login";
        }
    }
 */

    //TODO: 보고싶어요, 후기리스트의 경우 중앙에 배치하지 말고 가장 위에 배치한 뒤 스크롤할 수 있도록 수정하기
    @GetMapping("mypage")
    public String mypage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) model.addAttribute("loginId", userDetails.getUsername());

        //<홈> 탭
        PrincipalDetails principalDetails = (PrincipalDetails) userDetails;
        model.addAttribute("userName", principalDetails.getNickName());


        //<보고싶어요> 탭
        model.addAttribute("wantseeList", wantseeService.myWantseeList(userDetails.getUsername()));

        //<후기리스트> 탭
        model.addAttribute("reviewList", reviewService.SearchMyReview(userDetails.getUsername()));


        return "mypage";
    }

    @PostMapping("modifyMyInfo")
    public String modifyMyInfo(String userId, String userName, Errors errors, @AuthenticationPrincipal UserDetails userDetails) {
        PrincipalDetails principalDetails = (PrincipalDetails) userDetails;

        //값이 변경되지 않은 경우
        if(userId == userDetails.getUsername() && userName == principalDetails.getNickName()) return "변경된 내용이 없습니다.";

        //값이 변경된 경우
        else{
            //값 검증
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setUsername(userId);
            memberDTO.setNickname(userName);
            signUpFormValidator.validate(memberDTO, errors); //중복을 포함한 유효성 검사

            if (errors.hasErrors()) {
                //유효성 통과 못한 필드와 메시지를 핸들링
                Map<String, String> validatorResult = memberService.validateHandling(errors);
                for (String key : validatorResult.keySet()) {
                    return validatorResult.get(key);
                }

                return "유효성을 통과하지 못했습니다.";
            }
            else return "정상적으로 변경되었습니다.";
        }
    }

    @GetMapping("main")
    public String main(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) model.addAttribute("loginId", userDetails.getUsername());

        //띄울 영화 정보
        List<MovieinfoDTO> list = movieinfoService.SearchMainMovie();

        /*
        관객수 top5 영화 등은 변동이 적기 때문에 별도의 테이블을 만들고 캐싱 등을 사용하는 방식으로 지금처럼 하나하나 조회하는 것이 옳지 않다고 생각하나
        포스터는 별도의 api를 사용하는 관계로 지금과 같이 로직을 구현
         */

        model.addAttribute("top1", list.get(0));
        model.addAttribute("top2", list.get(1));
        model.addAttribute("top3", list.get(2));
        model.addAttribute("top4", list.get(3));
        model.addAttribute("top5", list.get(4));
        return "main";
    }

    @GetMapping("sign-up")
    public String signup(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) return "main"; //이미 로그인한 경우 메인으로
        return "sign-up";
    }

    @PostMapping("sign-up")
    public String signupFun(@Valid MemberDTO memberDTO, Errors errors, Model model) {
        signUpFormValidator.validate(memberDTO, errors);
        if (errors.hasErrors()) {
            // 회원가입 실패 시 입력 데이터를 유지
            model.addAttribute("memberDTO", memberDTO);

            //유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "sign-up";
        }
        memberService.saveMem(memberDTO);
        return "login";
    }

    @PostMapping("saveWantsee")
    public @ResponseBody void saveWantsee(@RequestParam("userId") String userId, @RequestParam("heartChk") int heartChk, @RequestParam("movName") String movName) {
        System.out.println("saveVocaTit 함수 들어옴 (컨트롤러)");
        wantseeService.saveWantsee(userId, heartChk, movName);
    }
}
