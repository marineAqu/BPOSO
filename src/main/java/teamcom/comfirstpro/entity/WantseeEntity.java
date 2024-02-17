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

    public static WantseeEntity toWantseeEntity (String loginId, Long movieNo) {
        WantseeEntity wantseeEntity = new WantseeEntity();

        wantseeEntity.setUserId(loginId);
        wantseeEntity.setMovNo(movieNo);

        return wantseeEntity;
    }
}
