package teamcom.comfirstpro.DTO;

import lombok.*;
import teamcom.comfirstpro.entity.WantseeEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WantseeDTO {
    private Long no; //후기 번호
    private String userId; //유저 아이디
    private Long movNo; //영화 번호
    private String movieNm; //영화 이름

    public static WantseeDTO toWantseeDTO(WantseeEntity wantseeEntity){
        WantseeDTO wantseeDTO = new WantseeDTO();

        wantseeDTO.setUserId(wantseeEntity.getUserId());
        wantseeDTO.setMovNo(wantseeEntity.getMovNo());
        wantseeDTO.setMovieNm(wantseeEntity.getMovieNm());

        return wantseeDTO;
    }
}
