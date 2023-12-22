package teamcom.comfirstpro.entity;
import teamcom.comfirstpro.DTO.MemberDTO;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "Member")
public class MemberEntity {
    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    //@Column(unique = true) // unique 제약조건 추가
    private String username; //아이디

    @Column
    private String memName; //이름

    @Column
    private String password;



    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUsername(memberDTO.getUsername());
        memberEntity.setPassword(memberDTO.getPassword());
        memberEntity.setMemName(memberDTO.getMemName());
        return memberEntity;
    }
}
