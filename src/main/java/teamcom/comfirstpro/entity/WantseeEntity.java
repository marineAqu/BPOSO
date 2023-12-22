package teamcom.comfirstpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Wantsee")
public class WantseeEntity {

    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long no; //후기 번호

    @Column
    private String userId; //유저 아이디

    @Column
    private Long movNo; //영화 번호

    @Column
    private String movieNm; //영화 이름
}
