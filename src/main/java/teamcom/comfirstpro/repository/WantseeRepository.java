package teamcom.comfirstpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamcom.comfirstpro.entity.ReviewEntity;
import teamcom.comfirstpro.entity.WantseeEntity;

import java.util.List;

public interface WantseeRepository  extends JpaRepository<ReviewEntity, Long> {
    List<WantseeEntity> findByMovNo(Long movNo);
}
