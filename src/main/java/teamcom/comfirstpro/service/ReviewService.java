package teamcom.comfirstpro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieinfoRepository movieinfoRepository;
    public void saveReview(String reviewContent, String loginId, double starpoint, Long movNo) {
        ReviewEntity reviewEntity = new ReviewEntity();

        reviewEntity.setReviewCon(reviewContent);
        reviewEntity.setRate(starpoint);
        reviewEntity.setUserId(loginId);
        reviewEntity.setMovNo(movNo);

        //이미 존재하면 기본키도 넣어서 update
        Optional<ReviewEntity> myReview = reviewRepository.findByUserIdAndMovNo(loginId, movNo);
        if(myReview.isPresent()) reviewEntity.setNo(myReview.get().getNo());

        //영화 이름도 받아야 함
        Optional<MovieinfoEntity> byNo = movieinfoRepository.findByNo(movNo);
        MovieinfoEntity movieinfoEntity = byNo.get();
        MovieinfoDTO dto = MovieinfoDTO.toMovieinfoDTO(movieinfoEntity);
        reviewEntity.setMovieNm(dto.getMovieNm());

        reviewRepository.save(reviewEntity);
    }

    public List<ReviewDTO> SearchReview(Long movieNo) {
        List<ReviewEntity> reviewEntityList = reviewRepository.findByMovNo(movieNo);

        List<ReviewDTO> reviewDTOList = new ArrayList<>();
        for(ReviewEntity reviewEntity: reviewEntityList) reviewDTOList.add(ReviewDTO.toReviewDTO(reviewEntity));

        return reviewDTOList;
    }

    public List<ReviewEntity> SearchMyReview(String userId) {
        List<ReviewEntity> reviewEntityList = reviewRepository.findByUserId(userId);

        return reviewEntityList;
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

    public Optional<ReviewEntity> ExitMyReview(Long movieNo, String userId) {
        return reviewRepository.findByUserIdAndMovNo(userId, movieNo);
    }

    public void deleteMyReview(String userId, String movName){
        reviewRepository.deleteByUserIdAndMovieNm(userId, movName);
    }
}
