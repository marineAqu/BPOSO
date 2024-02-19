package teamcom.comfirstpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamcom.comfirstpro.entity.WantseeEntity;

import java.util.List;

public interface WantseeRepository  extends JpaRepository<WantseeEntity, Long> {
    List<WantseeEntity> findByMovNo(Long movNo);
    Boolean existsByUserIdAndMovNo(String userId, Long movNo);
    void deleteByUserIdAndMovNo(String userId, Long movNo);
    List<WantseeEntity> findByUserId(String userId);
}
