package teamcom.comfirstpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamcom.comfirstpro.entity.ReviewEntity;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByMovNo(Long movNo);

    List<ReviewEntity> findByUserId(String userId);

    @Query("SELECT avg(m.rate) FROM ReviewEntity m WHERE m.movNo = :movNo")
    double findAverageRateByMovNo(@Param("movNo") Long movNo);

}
