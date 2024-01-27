package teamcom.comfirstpro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import teamcom.comfirstpro.DTO.MemberDTO;
import teamcom.comfirstpro.entity.MemberEntity;
import teamcom.comfirstpro.repository.MemberRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional //오류 발견 시 변경된 데이터를 변경 전으로 콜백
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입 vaild 메시지 핸들링
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    //회원가입
    public void saveMem(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO, passwordEncoder);
        memberRepository.save(memberEntity);
    }

    //스프링시큐리티 사용 이전 로그인 서비스
/*
    public MemberDTO login(MemberDTO memberDTO) {
            //1. 회원이 입력한 아이디로 DB에서 조회를 함
            //2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단

        Optional<MemberEntity> byMemId = memberRepository.findByUsername(memberDTO.getUsername());

        if (byMemId.isPresent()) {
            // 조회 결과가 있다(해당 아이디를 가진 회원 정보가 있다)
            MemberEntity memberEntity = byMemId.get();
            if (memberEntity.getPassword().equals(memberDTO.getPassword())) {
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                // 비밀번호 불일치(로그인실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 아이디를 가진 회원이 없다)
            return null;
        }
    }

 */

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
            // MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            // memberDTOList.add(memberDTO);
        }
        return memberDTOList;
    }


    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }
}
