package teamcom.comfirstpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamcom.comfirstpro.DTO.MemberDTO;


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
    private String nickname; //이름

    @Column
    private String password;


    public static MemberEntity toMemberEntity(MemberDTO memberDTO, PasswordEncoder passwordEncoder) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUsername(memberDTO.getUsername());
        memberEntity.setPassword(passwordEncoder.encode(memberDTO.getPassword())); //비밀번호 암호화
        memberEntity.setNickname(memberDTO.getNickname());
        return memberEntity;
    }
}
