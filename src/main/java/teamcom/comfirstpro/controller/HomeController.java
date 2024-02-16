package teamcom.comfirstpro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import teamcom.comfirstpro.DTO.MovieinfoDTO;
import teamcom.comfirstpro.service.MovieinfoService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MovieinfoService movieinfoService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails){
        //로그인 정보
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
}
