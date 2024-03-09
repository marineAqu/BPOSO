package teamcom.comfirstpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import teamcom.comfirstpro.DTO.MemberDTO;
import teamcom.comfirstpro.principal.PrincipalDetailsService;
import teamcom.comfirstpro.service.MemberService;
import teamcom.comfirstpro.validator.SignUpFormValidator;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final SignUpFormValidator signUpFormValidator;
    private final PrincipalDetailsService principalDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("sign-up")
    public String signup(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) return "showError"; //이미 로그인했을 경우 에러 페이지로
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

    @GetMapping("login")
    public String login(HttpServletRequest request, HttpSession session, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails != null) return "showError"; //이미 로그인했을 경우 에러 페이지로

        //로그인에 실패해서 request.getHeader("Referer")가 로그인 페이지일 경우 세션의 lastpage를 갱신하지 않음 (로그인 성공 후에 로그인 페이지를 이전 페이지로 인식해서 다시 로그인페이지로 돌아가지 않도록)
        //request.getHeader("Referer")가 null인 경우 예외처리
        try {
            if (!request.getHeader("Referer").equals("http://localhost:8080/login"))
                session.setAttribute("lastPage", request.getHeader("Referer")); //로그인 성공 시 이전 페이지로 이동
        }catch (NullPointerException e){

        }
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

}
