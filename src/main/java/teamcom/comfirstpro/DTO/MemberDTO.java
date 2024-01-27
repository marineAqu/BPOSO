package teamcom.comfirstpro.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import teamcom.comfirstpro.entity.MemberEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private Long id;

    @Size(min = 3, max = 10, message = "최소 3자 이상, 10자 이하 영문 입력")
    @Pattern(regexp = "^[a-z0-9]*$", message = "알파벳 소문자(a~z), 숫자(0~9)만 작성")
    @NotBlank
    private String username; //아이디

    @Size(min = 1, max = 10, message = "최소 1자 이상, 10자 이하 입력")
    @NotBlank
    private String nickname; //닉네임

    @Size(min = 4, max = 10, message = "최소 4자 이상, 10자 이하 입력")
    @NotBlank
    private String password; //비밀번호

    private String role;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setId(memberEntity.getId());
        memberDTO.setUsername(memberEntity.getUsername());
        memberDTO.setPassword(memberEntity.getPassword());
        memberDTO.setNickname(memberEntity.getNickname());
        return memberDTO;
    }

}
