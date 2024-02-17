package teamcom.comfirstpro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamcom.comfirstpro.repository.WantseeRepository;

@Service
@RequiredArgsConstructor
public class WantseeService {

    private final WantseeRepository wantseeRepository;

    public int IsMovieWantsee(String userId, Long movNo){

        //이미 보고싶어요 표시를 했을 시 1, 아닐 시 0

        if(wantseeRepository.existsByUserIdAndMovNo(userId, movNo) == true) return 1;
        else return 0;
    }
}
