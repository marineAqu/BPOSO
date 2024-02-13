package teamcom.comfirstpro.DTO;

import lombok.*;
import teamcom.comfirstpro.entity.MovieinfoEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieinfoDTO {

    private Long no;
    private String movieNm; //영화 이름
    private String drctrNm; //감독명
    private String makrNm; //제작사명
    private String incmeCmpnyNm; //수입회사명
    private String distbCmpnyNm; //유통회사명
    private String opnDe; //개방일자
    private String movieTyNm; //영화유형명
    private String movieStleNm; //영화형태명
    private String nltyNm; //국적명
    private String totScrnCo; //총스크린수
    private String salesPrice; //매출금액
    private int viewngNmprCo; //관람인원수
    private String genreNm; //장르명
    private String gradNm; //등급명
    private String movieSdivNm; //영화구분명

    public static MovieinfoDTO toMovieinfoDTO(MovieinfoEntity movieinfoEntity){
        MovieinfoDTO movieinfoDTO = new MovieinfoDTO();

        //영화제목, 감독이름, 개봉일, 등급을 set
        movieinfoDTO.setNo(movieinfoEntity.getNo());
        movieinfoDTO.setMovieNm(movieinfoEntity.getMovieNm());
        movieinfoDTO.setDrctrNm(movieinfoEntity.getDrctrNm());
        movieinfoDTO.setOpnDe(movieinfoEntity.getOpnDe());
        movieinfoDTO.setGradNm(movieinfoEntity.getGradNm());

        movieinfoDTO.setViewngNmprCo(movieinfoEntity.getViewngNmprCo());
        return movieinfoDTO;
    }
}