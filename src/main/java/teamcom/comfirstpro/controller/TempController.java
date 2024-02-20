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

    @GetMapping("mypage")
    public String mypage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) model.addAttribute("loginId", userDetails.getUsername());

        PrincipalDetails principalDetails = (PrincipalDetails) userDetails;
        model.addAttribute("userName", principalDetails.getNickName());

        //<홈> 탭


        //<보고싶어요> 탭
        model.addAttribute("wantseeList", wantseeService.myWantseeList(userDetails.getUsername()));

        //<후기리스트> 탭
        model.addAttribute("reviewList", reviewService.SearchMyReview(userDetails.getUsername()));


        return "mypage";
    }

    @GetMapping("main")
    public String main(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) model.addAttribute("loginId", userDetails.getUsername());
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
