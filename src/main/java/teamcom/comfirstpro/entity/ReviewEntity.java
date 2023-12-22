package teamcom.comfirstpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import teamcom.comfirstpro.DTO.ReviewDTO;

@Entity
@Setter
@Getter
@Table(name = "Review")
public class ReviewEntity {
    /* mysql create 때 작성한 내용
    * no integer auto_increment primary key,
    * rate double,
    * user_id varchar(20),
    * mov_no integer,
    * movie_nm text,
    * review_con text
    * */

    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long no; //후기 번호

    @Column
    private double rate; //별점

    @Column
    private String userId; //유저 아이디

    @Column
    private Long movNo; //영화 번호

    @Column
    private String movieNm; //영화 이름

    @Column
    private String reviewCon; //리뷰 내용

    public static ReviewEntity toReviewEntity(ReviewDTO reviewDTO){

        ReviewEntity reviewEntity = new ReviewEntity();

        reviewEntity.setRate(reviewDTO.getRate());
        reviewEntity.setUserId(reviewDTO.getUserId());
        reviewEntity.setMovNo(reviewDTO.getMovNo());
        reviewEntity.setMovieNm(reviewDTO.getMovieNm());
        reviewEntity.setReviewCon(reviewDTO.getReviewCon());

        return reviewEntity;
    }
}
