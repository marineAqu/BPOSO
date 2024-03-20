package teamcom.comfirstpro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.AopInvocationException;
import org.springframework.stereotype.Service;
import teamcom.comfirstpro.DTO.MovieinfoDTO;
import teamcom.comfirstpro.entity.MovieinfoEntity;
import teamcom.comfirstpro.repository.MovieinfoRepository;
import teamcom.comfirstpro.repository.ReviewRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieinfoService {

    private final MovieinfoRepository movieinfoRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    public List<MovieinfoDTO> SearchMovie(String searchName, List<String> genres, String sort) {
        //제목으로 검색
        List<MovieinfoEntity> movieinfoEntityList = movieinfoRepository.findByMovieNm(searchName);

        //검색 결과
        List<MovieinfoDTO> mav = new ArrayList<MovieinfoDTO>();
        mav = movieinfoEntityList.stream()
                .filter(movie -> genres == null || genres.isEmpty() || genres.contains(movie.getGenreNm()))
                .map(movie -> MovieinfoDTO.toMovieinfoDTO(movie))
                .collect(Collectors.toList());


        for(MovieinfoDTO movieinfoDTO: mav){
            movieinfoDTO.setAvgRate(reviewService.AvgReview(movieinfoDTO.getNo()));
        }

        // 관객 수에 따라 정렬
        if ("관객많은순".equals(sort)) Collections.sort(mav, Comparator.comparingInt(MovieinfoDTO::getViewngNmprCo).reversed());
        else if ("관객적은순".equals(sort)) Collections.sort(mav, Comparator.comparingInt(MovieinfoDTO::getViewngNmprCo));

        //별점에 따라 정렬
        else if("별점높은순".equals(sort)) Collections.sort(mav, Comparator.comparingDouble(MovieinfoDTO::getAvgRate).reversed());
        else if("별점낮은순".equals(sort)) Collections.sort(mav, Comparator.comparingDouble(MovieinfoDTO::getAvgRate));

        return mav;
    }

    public MovieinfoDTO SearchByNo(Long no) {
        Optional<MovieinfoEntity> byNo = movieinfoRepository.findByNo(no);
        MovieinfoEntity movieinfoEntity = byNo.get();

        MovieinfoDTO dto = MovieinfoDTO.toMovieinfoDTO(movieinfoEntity);

        return dto;
    }

    public List<MovieinfoDTO> SearchMainMovie(){

        List<MovieinfoEntity> list = movieinfoRepository.findTop5ByOrderByViewngNmprCoDesc();

        List<MovieinfoDTO> DTOList = new ArrayList<>();

        for (int i=0; i<5; i++) {
            DTOList.add(MovieinfoDTO.toMovieinfoDTO(list.get(i)));

            //평균 별점 가져오기
            try{
                DTOList.get(i).setAvgRate(reviewRepository.findAverageRateByMovNo(DTOList.get(i).getNo()));
            }catch (AopInvocationException e){
                DTOList.get(i).setAvgRate(-1.0);
            }
        }

        return DTOList;

    }

}
