package teamcom.comfirstpro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamcom.comfirstpro.entity.WantseeEntity;
import teamcom.comfirstpro.repository.MovieinfoRepository;
import teamcom.comfirstpro.repository.WantseeRepository;

@Service
@RequiredArgsConstructor
public class WantseeService {

    private final WantseeRepository wantseeRepository;
    private final MovieinfoRepository movieinfoRepository;

    public int IsMovieWantsee(String userId, Long movNo){

        //이미 보고싶어요 표시를 했을 시 1, 아닐 시 0

        if(wantseeRepository.existsByUserIdAndMovNo(userId, movNo) == true) return 1;
        else return 0;
    }

    @Transactional
    public void saveWantsee(String loginId, int heartChk, String movName){

        Long movieNo = movieinfoRepository.findNoByMovieNm(movName).getNo();

        //이미 보고싶어요가 눌린 경우 보고싶어요 삭제
        if(heartChk == 1) wantseeRepository.deleteByUserIdAndMovNo(loginId, movieNo);

        //보고싶어요가 이미 눌려있지 않은 경우 보고싶어요 저장
        else {
            WantseeEntity wantseeEntity = WantseeEntity.toWantseeEntity(loginId, movieNo);
            wantseeRepository.save(wantseeEntity);
        }
    }
}
