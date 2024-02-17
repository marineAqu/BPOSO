package teamcom.comfirstpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamcom.comfirstpro.entity.MovieinfoEntity;

import java.util.List;
import java.util.Optional;

public interface MovieinfoRepository extends JpaRepository<MovieinfoEntity, Long> {
    //제목에 searchName가 포함된 것을 검색
    @Query("SELECT m FROM MovieinfoEntity m WHERE m.movieNm LIKE %:searchName%")
    List<MovieinfoEntity> findByMovieNm(@Param("searchName") String searchName);
    //List<MovieinfoEntity> findByMovieNm(String searchName);

    Optional<MovieinfoEntity> findByNo(Long no);

    MovieinfoEntity findNoByMovieNm(String movieNm);

    List<MovieinfoEntity> findTop5ByOrderByViewngNmprCoDesc();
}