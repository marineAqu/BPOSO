package teamcom.comfirstpro.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import teamcom.comfirstpro.DTO.MemberDTO;
import teamcom.comfirstpro.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final MemberRepository memberRepository;
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(MemberDTO.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        MemberDTO memberDTO = (MemberDTO) object;
        if(memberRepository.existsByNickname(memberDTO.getNickname())){
            errors.rejectValue("nickname", "invalid.nickname",
                    new Object[]{memberDTO.getNickname()}, "이미 사용중인 닉네임입니다.");
        }

        if(memberRepository.existsByUsername(memberDTO.getUsername())){
            errors.rejectValue("username", "invalid.username",
                    new Object[]{memberDTO.getUsername()}, "이미 사용중인 아이디입니다.");
        }
    }

    //마이페이지에서 정보 수정 시 사용하는 validate, 중복 검사 시 본인은 제외
    public void modifiedValidate(Object object, Errors errors, Long id) {
        MemberDTO memberDTO = (MemberDTO) object;

        //findBy 결과가 비어있지 않고 결과가 본인 (=수정하지 않은 경우)이 아닌 경우에 errors 표시
        memberRepository.findByNickname(memberDTO.getNickname()).ifPresent(memberEntity -> {
            if (memberEntity.getId() != id) {
                errors.rejectValue("nickname", "invalid.nickname",
                        new Object[]{memberDTO.getNickname()}, "이미 사용중인 닉네임입니다.");
            }
        });

        try {
            if(memberRepository.findByUsername(memberDTO.getUsername()).getId() != id){
                errors.rejectValue("username", "invalid.username",
                        new Object[]{memberDTO.getUsername()}, "이미 사용중인 아이디입니다.");
            }
        }catch (NullPointerException e){}
    }
}