package teamcom.comfirstpro.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import teamcom.comfirstpro.DTO.MemberDTO;
import teamcom.comfirstpro.service.MemberService;
import teamcom.comfirstpro.validator.SignUpFormValidator;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TempController {

    private final MemberService memberService;
    private final SignUpFormValidator signUpFormValidator;

    @GetMapping("search-result")
    public String searchResult(Model model, HttpSession session) {

        return "search-result";
    }

    @GetMapping("login")
    public String login(Model model, HttpSession session) {

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

    @GetMapping("checkpage")
    public String checkpage(Model model, HttpSession session) {
        System.out.println("여기는 체크페이지 getMapping:"+session.getAttribute("loginId"));
        return "checkpage";
    }

    @GetMapping("mypage")
    public String mypage(Model model, HttpSession session) {
        return "mypage";
    }

    @GetMapping("main")
    public String main(Model model, HttpSession session) {
        System.out.print("메인에서 테스트: "+session.getAttribute("loginId"));
        return "main";
    }

    @GetMapping("header-temp")
    public String headerTemp(Model model, HttpSession session) {
        return "header-temp";
    }

    @GetMapping("/member/{id}")
    public void findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
    }

    @GetMapping("sign-up")
    public String signup(Model model) {
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
}
