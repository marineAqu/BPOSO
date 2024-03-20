package teamcom.comfirstpro.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamcom.comfirstpro.DTO.MovieinfoDTO;
import teamcom.comfirstpro.DTO.ReviewDTO;
import teamcom.comfirstpro.entity.ReviewEntity;
import teamcom.comfirstpro.service.MovieinfoService;
import teamcom.comfirstpro.service.ReviewService;
import teamcom.comfirstpro.service.WantseeService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final MovieinfoService movieinfoService;
    private final ReviewService reviewService;
    private final WantseeService wantseeService;


    @PostMapping("PostReview")
    public String GetPostReview(@RequestParam("reviewContent") String reviewContent, ReviewDTO reviewDTO,
                                @RequestParam("no") Long no,
                                @RequestParam("starpoint") double starpoint,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        //전달은 잘 됨: 아이디와 리뷰 글, 별점 내용 모두
        System.out.println(reviewContent);
        System.out.println(userDetails.getUsername());
        System.out.println(starpoint);

        //서비스로 넘어가서 후기 저장 | 리뷰글, 유저id, 별점, 영화번호
        reviewService.saveReview(reviewContent, userDetails.getUsername(), starpoint, no);

        //제출하면 새로고침되면서 영화정보가 없는 빈페이지로 가는 오류 있음 동일한 영화로 새로고침되도록 고쳐야 한다는 것 -> 해결(redirect)
        return "redirect:/review?movieNo="+no;
    }


    @GetMapping("review")
    public String review(@RequestParam("movieNo") Long movieNo,
                         Model model, @AuthenticationPrincipal UserDetails userDetails) {
        //로그인 되어있다면
        if(userDetails != null) {
            model.addAttribute("loginId", userDetails.getUsername());

            //보고싶어요 여부
            model.addAttribute("wantWatch", wantseeService.IsMovieWantsee(userDetails.getUsername(), movieNo));

            Optional<ReviewEntity> myReview = reviewService.ExitMyReview(movieNo, userDetails.getUsername());
            //이미 해당 영화에 리뷰를 작성했는지 확인
            if(myReview.isPresent()) {
                model.addAttribute("isReviewExist", 1);
                model.addAttribute("myReviewText", myReview.get().getReviewCon());
                model.addAttribute("myReviewRate", myReview.get().getRate());
            }
        }


        MovieinfoDTO movieinfoDTO;

        movieinfoDTO = movieinfoService.SearchByNo(movieNo);

        //영화 이름, 감독 이름, 개봉일
        model.addAttribute("no", movieNo);
        model.addAttribute("movNm", movieinfoDTO.getMovieNm());
        model.addAttribute("drctr", movieinfoDTO.getDrctrNm());
        model.addAttribute("opd", movieinfoDTO.getOpnDe());
        model.addAttribute("rateAvg", reviewService.AvgReview(movieNo));

        //검색 결과에 해당하는 list를 전달하고 search-result 페이지로 이동
        model.addAttribute("reviewList", reviewService.SearchReview(movieNo));

        return "review";
    }

    @PostMapping("deleteMyReview")
    public @ResponseBody void deleteMyReview(@RequestParam("userId") String userId, @RequestParam("movName") String movName) {

        reviewService.deleteMyReview(userId, movName);
    }

}
