package teamcom.comfirstpro.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teamcom.comfirstpro.DTO.MovieinfoDTO;
import teamcom.comfirstpro.DTO.ReviewDTO;
import teamcom.comfirstpro.service.MovieinfoService;
import teamcom.comfirstpro.service.ReviewService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieinfoController {

    private final MovieinfoService movieinfoService;
    private final ReviewService reviewService;

    //header-temp에서 돋보기 버튼(검색버튼)을 눌렀을 때
    @PostMapping("SearchMovie")
    public String getSearchMovie(@RequestParam("searchName") String searchName,
                                 @RequestParam(value = "genre", required = false) List<String> genres,
                                 @RequestParam("sort") String sort,
                                 Model model) {

        //검색 결과에 해당하는 list를 전달하고 tempof-viewmovie 페이지로 이동
        model.addAttribute("movieList", movieinfoService.SearchMovie(searchName, genres, sort));
        return "search-result";
        //return "tempof-viewmovie";
    }

    @PostMapping("PostReview")
    public String GetPostReview(@RequestParam("reviewContent") String reviewContent, ReviewDTO reviewDTO,
                                @RequestParam("no") Long no,
                                @RequestParam("starpoint") double starpoint, HttpSession session, Model model) {

        //전달은 잘 됨: 아이디와 리뷰 글, 별점 내용 모두
        System.out.println(reviewContent);
        System.out.println(session.getAttribute("loginId"));
        System.out.println(starpoint);

        //서비스로 넘어가서 후기 저장 | 리뷰글, 유저id, 별점, 영화번호
        reviewService.saveReview(reviewContent, session.getAttribute("loginId").toString(), starpoint, no);

        //제출하면 새로고침되면서 영화정보가 없는 빈페이지로 가는 오류 있음 동일한 영화로 새로고침되도록 고쳐야 함
        //return "review";
        return "redirect:/review?movieNo="+no;
    }

    @GetMapping("tempof-viewmovie")
    public String tempofviewmovie(Model model, HttpSession session) {

        return "tempof-viewmovie";
    }
    @GetMapping("review")
    public String review(@RequestParam("movieNo") Long movieNo,
                         Model model, HttpSession session) {
        MovieinfoDTO movieinfoDTO;

        //System.out.println(movieNo);
        //session.setAttribute("loginId", session.getAttribute("loginId"));

        movieinfoDTO = movieinfoService.SearchByNo(movieNo);

        //영화 이름, 감독 이름, 개봉일
        model.addAttribute("no", movieNo);
        model.addAttribute("movNm", movieinfoDTO.getMovieNm());
        model.addAttribute("drctr", movieinfoDTO.getDrctrNm());
        model.addAttribute("opd", movieinfoDTO.getOpnDe());

        //검색 결과에 해당하는 list를 전달하고 tempof-viewmovie 페이지로 이동
        model.addAttribute("reviewList", reviewService.SearchReview(movieNo));

        return "review";
    }

    @GetMapping("mypage-like")
    public String mypageLike(Model model, HttpSession session) {
        return "mypage-like";
    }
    @GetMapping("mypage-main")
    public String mypageMain(Model model, HttpSession session) {
        return "mypage-main";

    }
    @GetMapping("mypage-review")
    public String mypageReview(Model model, HttpSession session) {
        return "mypage-review";
    }
}
