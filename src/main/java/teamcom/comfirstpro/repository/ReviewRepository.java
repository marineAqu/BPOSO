package teamcom.comfirstpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamcom.comfirstpro.entity.ReviewEntity;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByMovNo(Long movNo);
}
