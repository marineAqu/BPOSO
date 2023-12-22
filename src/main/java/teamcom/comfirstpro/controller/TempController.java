package teamcom.comfirstpro.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamcom.comfirstpro.DTO.MemberDTO;
import teamcom.comfirstpro.service.MemberService;

@Controller
@RequiredArgsConstructor
public class TempController {

    private final MemberService memberService;

    @GetMapping("search-result")
    public String searchResult(Model model, HttpSession session) {

        return "search-result";
    }

    @GetMapping("login")
    public String login(Model model, HttpSession session) {

        return "login";
    }

    @PostMapping("login")
    public String loginFun(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);

        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginId", loginResult.getUsername());

            System.out.print(session.getAttribute("loginId"));
            return "main";
        } else {
            // login 실패
            return "login";
        }
    }
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
    public String signupFun(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "login";
    }

    @PostMapping("id-check")
    public @ResponseBody String idCheck(@RequestParam("memId") String memId) {
        System.out.println("memberEmail = " + memId);
        String checkResult = memberService.idCheck(memId);
        return checkResult;
    }
}
