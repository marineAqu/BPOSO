package teamcom.comfirstpro.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(HttpSession session){
        if(session.getAttribute("loginId") == null) session.setAttribute("loginId", "n");
        /*
        if (authentication != null && authentication.isAuthenticated()) {
            // 로그인한 경우
            System.out.println("로그인됨: 시큐리티");
        } else {
            // 로그인하지 않은 경우
            System.out.println("로그인 되지않음: 시큐리티");
        }
         */

        return "main";
    }
}
