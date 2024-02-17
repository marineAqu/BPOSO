package teamcom.comfirstpro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamcom.comfirstpro.DTO.MovieinfoDTO;
import teamcom.comfirstpro.DTO.ReviewDTO;
import teamcom.comfirstpro.entity.MovieinfoEntity;
import teamcom.comfirstpro.entity.ReviewEntity;
import teamcom.comfirstpro.repository.MovieinfoRepository;
import teamcom.comfirstpro.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieinfoRepository movieinfoRepository;
    public void saveReview(String reviewContent, String loginId, double starpoint, Long no) {
        // 1. dto -> entity 변환
        // 2. repository의 save 메서드 호출

        //ReviewEntity reviewEntity = ReviewEntity.toReviewEntity(reviewDTO);

        ReviewEntity reviewEntity = new ReviewEntity();

        reviewEntity.setReviewCon(reviewContent);
        reviewEntity.setRate(starpoint);
        reviewEntity.setUserId(loginId);
        reviewEntity.setMovNo(no);
        //영화 이름도 받아야 함

        Optional<MovieinfoEntity> byNo = movieinfoRepository.findByNo(no);
        MovieinfoEntity movieinfoEntity = byNo.get();
        MovieinfoDTO dto = MovieinfoDTO.toMovieinfoDTO(movieinfoEntity);
        reviewEntity.setMovieNm(dto.getMovieNm());

        reviewRepository.save(reviewEntity);
        // repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)
    }

    public List<ReviewDTO> SearchReview(Long movieNo) {
        List<ReviewEntity> reviewEntityList = reviewRepository.findByMovNo(movieNo);

        List<ReviewDTO> reviewDTOList = new ArrayList<>();
        for(ReviewEntity reviewEntity: reviewEntityList) reviewDTOList.add(ReviewDTO.toReviewDTO(reviewEntity));

        return reviewDTOList;
    }

    public List<ReviewDTO> SearchMyReview(String userId) {
        List<ReviewEntity> reviewEntityList = reviewRepository.findByUserId(userId);

        List<ReviewDTO> reviewDTOList = new ArrayList<>();
        for(ReviewEntity reviewEntity: reviewEntityList) reviewDTOList.add(ReviewDTO.toReviewDTO(reviewEntity));

        return reviewDTOList;
    }


    public Double AvgReview(Long movieNo) {
        List<ReviewEntity> movies = reviewRepository.findByMovNo(movieNo);
        double avgReview = 0;

        for (ReviewEntity movie : movies) {
            avgReview += movie.getRate();
        }

        if (movies.size() > 0) return avgReview / movies.size();
        else return -1.0; //후기가 없을 경우 -1.0로 표시
    }
}
