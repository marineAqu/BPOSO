package teamcom.comfirstpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Movieinfo")
public class MovieinfoEntity {

    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long no;

    @Column
    private String movieNm; //영화 이름

    @Column
    private String drctrNm; //감독명

    @Column
    private String makrNm; //제작사명

    @Column
    private String incmeCmpnyNm; //수입회사명

    @Column
    private String distbCmpnyNm; //유통회사명

    @Column
    private String opnDe; //개방일자

    @Column
    private String movieTyNm; //영화유형명

    @Column
    private String movieStleNm; //영화형태명

    @Column
    private String nltyNm; //국적명

    @Column
    private String totScrnCo; //총스크린수

    @Column
    private String salesPrice; //매출금액

    @Column
    private int viewngNmprCo; //관람인원수

    @Column
    private String genreNm; //장르명

    @Column
    private String gradNm; //등급명

    @Column
    private String movieSdivNm; //영화구분명

}
