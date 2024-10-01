package project.boot.fideco.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.boot.fideco.dto.response.ResponseDTO;
import project.boot.fideco.cart.service.CartService;
import project.boot.fideco.dto.request.auth.CheckCertificationRequestDTO;
import project.boot.fideco.dto.request.auth.EmailCertificationRequestDTO;
import project.boot.fideco.dto.request.auth.IdCheckRequestDTO;
import project.boot.fideco.dto.request.auth.SignUpRequestDTO;
import project.boot.fideco.dto.response.auth.CheckCertificationResponseDTO;
import project.boot.fideco.dto.response.auth.EmailCertificationResponseDTO;
import project.boot.fideco.dto.response.auth.IdCheckResponseDTO;
import project.boot.fideco.dto.response.auth.SignUpResponseDTO;
import project.boot.fideco.member.certificationEntity.CertificationEntity;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.repository.CertificationRepository;
import project.boot.fideco.member.repository.MemberRepository;
import project.boot.fideco.provider.EmailProvider;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final CartService cartService; 

    private final EmailProvider emailProvider;
    private final CertificationRepository certificationRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 엔코더

    // 모든 회원 조회
    public List<MemberEntity> getAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 ID로 회원 정보 조회
    public Optional<MemberEntity> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // 회원 ID로 회원 조회 (Optional 반환)
    public Optional<MemberEntity> getMemberByMemberId(String memberId) {
        return Optional.ofNullable(memberRepository.findByMemberId(memberId));
    }

    // 회원 정보 업데이트
    public MemberEntity updateMember(MemberEntity member) {
        // 기존 회원 정보 조회
        Optional<MemberEntity> existingMember = memberRepository.findById(member.getId());
        if (existingMember.isPresent()) {
            MemberEntity existing = existingMember.get();
            // 비밀번호가 변경되었을 경우 암호화 처리
            if (!member.getMemberPw().equals(existing.getMemberPw())) {
                member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));
            }
            // 회원 정보 저장
            return memberRepository.save(member);
        } else {
            throw new RuntimeException("Member not found"); // 회원이 없을 경우 예외 발생
        }
    }

 // 회원 삭제 처리 메서드
    @Transactional
    public void deleteMember(Long id) {
        Optional<MemberEntity> member = memberRepository.findById(id);
        if (member.isPresent()) {
            String memberId = member.get().getMemberId();
            // 회원 탈퇴 시 해당 회원의 장바구니 삭제
            cartService.deleteCartByMemberId(memberId);
            memberRepository.deleteById(id);
        } else {
            throw new RuntimeException("Member not found");
        }
    }

    // 아이디 중복 확인
    public ResponseEntity<? super IdCheckResponseDTO> idcheck(IdCheckRequestDTO dto) {
        try {
            String memberId = dto.getMemberId();
            // 아이디 중복 여부 확인
            boolean isExistId = memberRepository.existsByMemberId(memberId);
            if (isExistId) {
                return IdCheckResponseDTO.duplicateId(); // 중복일 경우 중복 응답
            } else {
                return IdCheckResponseDTO.success(); // 성공 응답
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError(); // 데이터베이스 오류 응답
        }
    }

    // 이메일 인증 요청 처리
    public ResponseEntity<? super EmailCertificationResponseDTO> emailCertification(EmailCertificationRequestDTO dto) {
        try {
            String memberId = dto.getMemberId();
            String email = dto.getMemberEmail();

            // 아이디 중복 확인
            boolean isExistId = memberRepository.existsByMemberId(memberId);
            if (isExistId) return EmailCertificationResponseDTO.duplicateId(); // 중복일 경우 응답

            // 인증번호 생성 및 발송
            String certificationNumber = getCertificationNumber();
            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSuccessed) return EmailCertificationResponseDTO.mailSendFail(); // 이메일 발송 실패 시 응답

            // 인증 정보 저장
            CertificationEntity certificationEntity = new CertificationEntity(memberId, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError(); // 데이터베이스 오류 응답
        }
        return EmailCertificationResponseDTO.success(); // 성공 응답
    }

    // 인증번호 생성 로직 (6자리 숫자)
    private String getCertificationNumber() {
        StringBuilder certificationNumber = new StringBuilder();
        for (int count = 0; count < 6; count++)
            certificationNumber.append((int) (Math.random() * 10));
        return certificationNumber.toString();
    }

    // 인증번호 확인 처리
    public ResponseEntity<? super CheckCertificationResponseDTO> checkCertification(CheckCertificationRequestDTO dto) {
        try {
            String memberId = dto.getMemberId();
            String memberEmail = dto.getMemberEmail();
            String certificationNumber = dto.getCertificationNumber();

            // 인증 정보 조회
            CertificationEntity certificationEntity = certificationRepository.findByMemberId(memberId);
            if (certificationEntity == null)
                return CheckCertificationResponseDTO.certificationFail(); // 인증 정보가 없을 경우 실패 응답

            // 인증번호 및 이메일이 일치하는지 확인
            boolean isMatched = certificationEntity.getMemberEmail().equals(memberEmail) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return CheckCertificationResponseDTO.certificationFail(); // 불일치 시 실패 응답

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError(); // 데이터베이스 오류 응답
        }

        return CheckCertificationResponseDTO.success(); // 성공 응답
    }

    // 회원가입 처리
    public ResponseEntity<? super SignUpResponseDTO> signUp(SignUpRequestDTO dto) {
        try {
            String memberId = dto.getMemberId();
            System.out.println("Sign Up Request for Member ID: " + memberId);

            // 아이디 중복 확인
            boolean isExistId = memberRepository.existsByMemberId(memberId);
            if (isExistId) {
                System.out.println("Member ID already exists");
                return SignUpResponseDTO.duplicateId(); // 중복일 경우 응답
            }

            // 이메일 및 인증번호 확인
            String memberEmail = dto.getMemberEmail();
            String certificationNumber = dto.getCertificationNumber();

            // 인증 정보 조회
            CertificationEntity certificationEntity = certificationRepository.findByMemberId(memberId);
            if (certificationEntity == null) {
                System.out.println("Certification entity not found for Member ID: " + memberId);
                return SignUpResponseDTO.certificationFail(); // 인증 정보가 없을 경우 실패 응답
            }

            // 인증번호 및 이메일 일치 여부 확인
            boolean isMatched = certificationEntity.getMemberEmail().equals(memberEmail) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) {
                System.out.println("Certification failed for Member ID: " + memberId);
                return SignUpResponseDTO.certificationFail(); // 불일치 시 실패 응답
            }

            // 비밀번호 암호화 및 회원 저장
            String memberPw = dto.getMemberPw();
            String encodedPassword = passwordEncoder.encode(memberPw);
            System.out.println("Raw Password: " + memberPw);
            System.out.println("Encoded Password: " + encodedPassword);
            dto.setMemberPw(encodedPassword);

            // 회원 정보 저장
            MemberEntity memberEntity = new MemberEntity(dto);
            memberRepository.save(memberEntity);
            System.out.println("Member saved: " + memberEntity.getMemberId());

            // 인증 정보 삭제
            certificationRepository.delete(certificationEntity);
            System.out.println("Certification entity deleted for Member ID: " + memberId);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError(); // 데이터베이스 오류 응답
        }
        return SignUpResponseDTO.success(); // 성공 응답
    }

    // 아이디 찾기 기능 (이메일로 아이디 발송)
    public boolean sendIdToEmail(String email) {
        Optional<MemberEntity> member = memberRepository.findByMemberEmail(email); // 이메일로 회원 조회
        if (member.isPresent()) {
            String memberId = member.get().getMemberId();
            return emailProvider.sendIdMail(email, memberId); // 아이디 이메일 발송
        }
        return false;
    }

    // 비밀번호 재설정 기능
    public boolean sendPasswordResetLink(String memberId, String email) {
        Optional<MemberEntity> member = memberRepository.findByMemberIdAndMemberEmail(memberId, email); // 아이디와 이메일로 회원 조회
        if (member.isPresent()) {
            // 임시 비밀번호 생성 및 암호화 후 저장
            String newPassword = emailProvider.generateRandomPassword();
            member.get().setMemberPw(passwordEncoder.encode(newPassword));
            memberRepository.save(member.get());
            return emailProvider.sendResetPasswordMail(email, newPassword); // 이메일로 임시 비밀번호 발송
        }
        return false;
    }

    // 이메일 중복 확인
    public boolean isEmailDuplicate(String memberEmail) {
        return memberRepository.existsByMemberEmail(memberEmail); // 이메일 중복 여부 확인
    }
}
