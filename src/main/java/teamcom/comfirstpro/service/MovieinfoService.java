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

    public List<MovieinfoDTO> SearchMovie(String searchName, List<String> genres, String sort) {
        //제목으로 검색
        List<MovieinfoEntity> movieinfoEntityList = movieinfoRepository.findByMovieNm(searchName);

        //검색 결과 (7.19 전 코드: 장르선택 X)
        //List<MovieinfoDTO> mav = new ArrayList<>();
        //검색 결과를 하나씩 mav에 붙임
        //for(MovieinfoEntity movieinfoEntity: movieinfoEntityList){
        //    mav.add(MovieinfoDTO.toMovieinfoDTO(movieinfoEntity));
        //}

        //검색 결과 (7.19 이후 코드: 장르선택 가능)
        List<MovieinfoDTO> mav = new ArrayList<MovieinfoDTO>();
        mav = movieinfoEntityList.stream()
                .filter(movie -> genres == null || genres.isEmpty() || genres.contains(movie.getGenreNm()))
                .map(movie -> MovieinfoDTO.toMovieinfoDTO(movie))
                .collect(Collectors.toList());

        /* 검증용
        for(MovieinfoDTO movieinfoDTO: mav){
            System.out.println(mav);
        }
        */

        // 관객 수에 따라 정렬: 7.19 이후 코드
        if ("관객많은순".equals(sort)) {
            Collections.sort(mav, Comparator.comparingInt(MovieinfoDTO::getViewngNmprCo).reversed());
            //mav.sort(Integer.parseInt(MovieinfoEntity.getViewngNmprCo()));
        }
        else if ("관객적은순".equals(sort)) {
            Collections.sort(mav, Comparator.comparingInt(MovieinfoDTO::getViewngNmprCo));
            //mav.sort(Comparator.comparingInt(Integer.parseInt(MovieinfoDTO::getViewngNmprCo)).reversed());
        }
        else System.out.println("별점 정렬 구현X"+sort);


        //mav.addObject("movieinfoEntityList", movieinfoEntityList);
        //mav.setViewName("header-temp");



        //System.out.println("검색키워드:" + searchName);
        return mav;
        //return movieinfoRepository.SearchMovie(searchName);
    }

    public void PostReview(String reviewContent, String loginId) {
        //loginId
        // 1. dto -> entity 변환
        // 2. repository의 save 메서드 호출

        //MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        //memberRepository.save(memberEntity);
        // repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)

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

/*
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
            // MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            // memberDTOList.add(memberDTO);
        }
        return memberDTOList;
    }


    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
//            MemberEntity memberEntity = optionalMemberEntity.get();
//            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
//            return memberDTO;
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }
    */

}
