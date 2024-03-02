package teamcom.comfirstpro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieinfoController {
    //header-temp에서 돋보기 버튼(검색버튼)을 눌렀을 때
    @PostMapping("SearchMovie")
    public String getSearchMovie(@RequestParam("searchName") String searchName,
                                 @RequestParam(value = "genre", required = false) List<String> genres,
                                 @RequestParam("sort") String sort,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if(userDetails != null) model.addAttribute("loginId", userDetails.getUsername());

        //검색 결과페이지에서 로그인 시 검색 결과 페이지로 돌아가지지 않는 문제 해결: 주소에 내용을 담음

        //model.addAttribute("movieList", movieinfoService.SearchMovie(searchName, genres, sort));
        //return "search-result";
        //return "search-result?searchName="+searchName+"&genres="+genres+"&sort="+sort;

        redirectAttributes.addAttribute("searchName", searchName);
        if (genres != null && !genres.isEmpty()) {
            redirectAttributes.addAttribute("genre", genres);
        }
        redirectAttributes.addAttribute("sort", sort);

        return "redirect:/search-result";
    }
}
