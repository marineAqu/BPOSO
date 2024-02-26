package teamcom.comfirstpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamcom.comfirstpro.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    //아이디로 회원 정보 조회 (select * from member_table where member_email=?)
    MemberEntity findByUsername(String memId);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

    Optional<MemberEntity> findByNickname(String nickname);

}