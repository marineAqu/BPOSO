package teamcom.comfirstpro.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("search-result")
    public String searchResult(@RequestParam("searchName") String searchName,
                               @RequestParam(value = "genre", required = false) List<String> genres,
                               @RequestParam("sort") String sort,
                               Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        if(userDetails != null) model.addAttribute("loginId", userDetails.getUsername());

        model.addAttribute("movieList", movieinfoService.SearchMovie(searchName, genres, sort));
        model.addAttribute("searchTitle", "\""+searchName+"\"");

        return "search-result";
    }

    //TODO: 보고싶어요, 후기리스트의 경우 중앙에 배치하지 말고 가장 위에 배치한 뒤 스크롤할 수 있도록 수정하기
    @GetMapping("mypage")
    public String mypage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) model.addAttribute("loginId", userDetails.getUsername());

        //<홈> 탭
        PrincipalDetails principalDetails = (PrincipalDetails) userDetails;
        model.addAttribute("userName", principalDetails.getNickName());
        model.addAttribute("ModLoginId", userDetails.getUsername());
        model.addAttribute("ModUserName", principalDetails.getNickName());


        //<보고싶어요> 탭
        model.addAttribute("wantseeList", wantseeService.myWantseeList(userDetails.getUsername()));
        //<후기리스트> 탭
        model.addAttribute("reviewList", reviewService.SearchMyReview(userDetails.getUsername()));


        return "mypage";
    }

    @PostMapping("modifyMyInfo")
    public String modifyMyInfo(@Valid MemberDTO memberDTO, Errors errors, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //기본적으로 추가되어야 하는 model 내용들
        model.addAttribute("userName", userDetails.getUsername());
        //<보고싶어요> 탭
        model.addAttribute("wantseeList", wantseeService.myWantseeList(userDetails.getUsername()));
        //<후기리스트> 탭
        model.addAttribute("reviewList", reviewService.SearchMyReview(userDetails.getUsername()));


        //비밀번호를 틀린 경우
        if(!memberService.passwordValid(memberDTO.getPassword(), userDetails.getPassword())) {

            model.addAttribute("loginId", userDetails.getUsername());
            model.addAttribute("userName", ((PrincipalDetails) principal).getNickName());
            model.addAttribute("ModLoginId", memberDTO.getUsername());
            model.addAttribute("ModUserName", memberDTO.getNickname());
            model.addAttribute("isModified", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("modifiedFail", "Y");

            return "mypage";
        }

        //변경사항이 없을 경우
        if(memberDTO.getNickname().equals(((PrincipalDetails) principal).getNickName()) && memberDTO.getUsername().equals(((PrincipalDetails) principal).getUsername())){
            model.addAttribute("loginId", userDetails.getUsername());
            model.addAttribute("userName", ((PrincipalDetails) principal).getNickName());
            model.addAttribute("ModLoginId", memberDTO.getUsername());
            model.addAttribute("ModUserName", memberDTO.getNickname());
            model.addAttribute("isModified", "수정된 내용이 없습니다.");
            model.addAttribute("modifiedFail", "Y");
            return "mypage";
        }

        //중복을 포함한 유효성 검사
        signUpFormValidator.modifiedValidate(memberDTO, errors, ((PrincipalDetails) principal).getNo());

        //닉네임과 아이디에 대한 오류가 있는지만 검증
        if (errors.hasFieldErrors("username") || errors.hasFieldErrors("nickname")) {
            //유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            //유효성을 통과하지 못한 경우
            model.addAttribute("loginId", userDetails.getUsername());
            model.addAttribute("userName", ((PrincipalDetails) principal).getNickName());
            model.addAttribute("isModified", "정상적으로 수정되지 않았습니다.");
            model.addAttribute("ModLoginId", memberDTO.getUsername());
            model.addAttribute("ModUserName", memberDTO.getNickname());
            model.addAttribute("modifiedFail", "Y");
            return "mypage";
        }
        else{
            memberService.modifiyMemInfo(memberDTO, ((PrincipalDetails) principal).getUsername());

            //유효성을 통과한 경우
            model.addAttribute("loginId", memberDTO.getUsername());
            model.addAttribute("userName", memberDTO.getNickname());
            model.addAttribute("ModLoginId", memberDTO.getUsername());
            model.addAttribute("ModUserName", memberDTO.getNickname());
            model.addAttribute("isModified", "정상적으로 수정되었습니다.");

            //정보가 변경되었으므로 세션을 갱신
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberDTO.getUsername(), memberDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "mypage";
        }
    }

    @PostMapping("saveWantsee")
    public @ResponseBody void saveWantsee(@RequestParam("userId") String userId, @RequestParam("heartChk") int heartChk, @RequestParam("movName") String movName) {
        System.out.println("saveVocaTit 함수 들어옴 (컨트롤러)");
        wantseeService.saveWantsee(userId, heartChk, movName);
    }
}
