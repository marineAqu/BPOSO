package teamcom.comfirstpro.DTO;

import lombok.*;
import teamcom.comfirstpro.entity.ReviewEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {

    private Long no; //후기 번호
    private double rate; //별점
    private String userId; //유저 아이디
    private Long movNo; //영화 번호
    private String movieNm; //영화 이름
    private String reviewCon; //리뷰 내용

    public static ReviewDTO toReviewDTO(ReviewEntity reviewEntity){
        ReviewDTO reviewDTO = new ReviewDTO();

        //영화제목, 감독이름, 개봉일, 등급을 set
        reviewDTO.setRate(reviewEntity.getRate());
        reviewDTO.setUserId(reviewEntity.getUserId());
        reviewDTO.setMovNo(reviewEntity.getMovNo());
        reviewDTO.setMovieNm(reviewEntity.getMovieNm());
        reviewDTO.setReviewCon(reviewEntity.getReviewCon());

        return reviewDTO;
    }
}
