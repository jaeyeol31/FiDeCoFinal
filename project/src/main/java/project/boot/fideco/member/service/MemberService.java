//package project.boot.fideco.member.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import lombok.RequiredArgsConstructor;
//import project.boot.fideco.dto.response.ResponseDTO;
//import project.boot.fideco.dto.request.auth.CheckCertificationRequestDTO;
//import project.boot.fideco.dto.request.auth.EmailCertificationRequestDTO;
//import project.boot.fideco.dto.request.auth.IdCheckRequestDTO;
//import project.boot.fideco.dto.request.auth.SignUpRequestDTO;
//import project.boot.fideco.dto.response.auth.CheckCertificationResponseDTO;
//import project.boot.fideco.dto.response.auth.EmailCertificationResponseDTO;
//import project.boot.fideco.dto.response.auth.IdCheckResponseDTO;
//import project.boot.fideco.dto.response.auth.SignUpResponseDTO;
//import project.boot.fideco.member.certificationEntity.CertificationEntity;
//import project.boot.fideco.member.entity.MemberEntity;
//import project.boot.fideco.member.repository.CertificationRepository;
//import project.boot.fideco.member.repository.MemberRepository;
//import project.boot.fideco.provider.EmailProvider;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class MemberService {
//
//	@Autowired
//	private final MemberRepository memberRepository;
//	private final EmailProvider emailProvider;
//	private final CertificationRepository certificationRepository;
//	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
////	public MemberEntity saveMember(MemberEntity member) {
////		
////		return memberRepository.save(member);
////	}
//
//	public List<MemberEntity> getAllMembers() {
//		return memberRepository.findAll();
//	}
//
//	public Optional<MemberEntity> getMemberById(Long id) {
//		return memberRepository.findById(id);
//	}
//
//	public Optional<MemberEntity> getMemberByMemberId(String memberId) {
//		return Optional.ofNullable(memberRepository.findByMemberId(memberId));
//	}
//
////	public MemberEntity updateMember(MemberEntity member) {
////		return memberRepository.save(member);
////	}
//	   public MemberEntity updateMember(MemberEntity member) {
//	        Optional<MemberEntity> existingMember = memberRepository.findById(member.getId());
//	        if (existingMember.isPresent()) {
//	            MemberEntity existing = existingMember.get();
//	            if (!member.getMemberPw().equals(existing.getMemberPw())) {
//	                member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));
//	            }
//	            return memberRepository.save(member);
//	        } else {
//	            throw new RuntimeException("Member not found");
//	        }
//	    }
//
//	public void deleteMember(Long id) {
//		memberRepository.deleteById(id);
//	}
//
//	public ResponseEntity<? super IdCheckResponseDTO> idcheck(IdCheckRequestDTO dto) {
//        try {
//            String memberId = dto.getMemberId();
//            boolean isExistId = memberRepository.existsByMemberId(memberId);
//            if (isExistId) {
//                return IdCheckResponseDTO.duplicateId();
//            } else {
//                return IdCheckResponseDTO.success();
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            return ResponseDTO.databaseError();
//        }
//    }
//
//	public ResponseEntity<? super EmailCertificationResponseDTO> emailCertification(EmailCertificationRequestDTO dto) {
//		try {
//			String memberId = dto.getMemberId();
//			String email = dto.getMemberEmail();
//
//			boolean isExistId = memberRepository.existsByMemberId(memberId);
//			if (isExistId)
//				return EmailCertificationResponseDTO.duplicateId();
//
//			String certificationNumber = getCertificationNumber();
//
//			boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
//			if (!isSuccessed)
//				return EmailCertificationResponseDTO.mailSendFail();
//
//			CertificationEntity certificationEntity = new CertificationEntity(memberId, email, certificationNumber);
//			certificationRepository.save(certificationEntity);
//
//		} catch (Exception exception) {
//			exception.printStackTrace();
//			return ResponseDTO.databaseError();
//		}
//		return EmailCertificationResponseDTO.success();
//	}
//
//	private String getCertificationNumber() {
//		StringBuilder certificationNumber = new StringBuilder();
//		for (int count = 0; count < 6; count++)
//			certificationNumber.append((int) (Math.random() * 10));
//		return certificationNumber.toString();
//	}
//
//	public ResponseEntity<? super CheckCertificationResponseDTO> checkCertification(CheckCertificationRequestDTO dto) {
//		try {
//			String memberId = dto.getMemberId();
//			String memberEmail = dto.getMemberEmail();
//			String certificationNumber = dto.getCertificationNumber();
//
//			CertificationEntity certificationEntity = certificationRepository.findByMemberId(memberId);
//			if (certificationEntity == null)
//				return CheckCertificationResponseDTO.certificationFail();
//
//			boolean isMatched = certificationEntity.getMemberEmail().equals(memberEmail)
//					&& certificationEntity.getCertificationNumber().equals(certificationNumber);
//			if (!isMatched)
//				return CheckCertificationResponseDTO.certificationFail();
//
//		} catch (Exception exception) {
//			exception.printStackTrace();
//			return ResponseDTO.databaseError();
//		}
//
//		return CheckCertificationResponseDTO.success();
//	}
//
//	public ResponseEntity<? super SignUpResponseDTO> signUp(SignUpRequestDTO dto) {
//	    try {
//	        String memberId = dto.getMemberId();
//	        System.out.println("Sign Up Request for Member ID: " + memberId);
//
//	        boolean isExistId = memberRepository.existsByMemberId(memberId);
//	        if (isExistId) {
//	            System.out.println("Member ID already exists");
//	            return SignUpResponseDTO.duplicateId();
//	        }
//
//	        String memberEmail = dto.getMemberEmail();
//	        String certificationNumber = dto.getCertificationNumber();
//
//	        CertificationEntity certificationEntity = certificationRepository.findByMemberId(memberId);
//	        if (certificationEntity == null) {
//	            System.out.println("Certification entity not found for Member ID: " + memberId);
//	            return SignUpResponseDTO.certificationFail();
//	        }
//
//	        boolean isMatched = certificationEntity.getMemberEmail().equals(memberEmail)
//	                && certificationEntity.getCertificationNumber().equals(certificationNumber);
//	        if (!isMatched) {
//	            System.out.println("Certification failed for Member ID: " + memberId);
//	            return SignUpResponseDTO.certificationFail();
//	        }
//
//	        String memberPw = dto.getMemberPw();
//	        String encodedPassword = passwordEncoder.encode(memberPw);
//	        System.out.println("Raw Password: " + memberPw);
//	        System.out.println("Encoded Password: " + encodedPassword);
//	        dto.setMemberPw(encodedPassword);
//
//	        MemberEntity memberEntity = new MemberEntity(dto);
//	        memberRepository.save(memberEntity);
//	        System.out.println("Member saved: " + memberEntity.getMemberId());
//
//	        certificationRepository.delete(certificationEntity);
//	        System.out.println("Certification entity deleted for Member ID: " + memberId);
//
//	    } catch (Exception exception) {
//	        exception.printStackTrace();
//	        return ResponseDTO.databaseError();
//	    }
//	    return SignUpResponseDTO.success();
//	}
//
//}


package project.boot.fideco.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.boot.fideco.dto.response.ResponseDTO;
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
    private final EmailProvider emailProvider;
    private final CertificationRepository certificationRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<MemberEntity> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<MemberEntity> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<MemberEntity> getMemberByMemberId(String memberId) {
        return Optional.ofNullable(memberRepository.findByMemberId(memberId));
    }

    public MemberEntity updateMember(MemberEntity member) {
        Optional<MemberEntity> existingMember = memberRepository.findById(member.getId());
        if (existingMember.isPresent()) {
            MemberEntity existing = existingMember.get();
            if (!member.getMemberPw().equals(existing.getMemberPw())) {
                member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));
            }
            return memberRepository.save(member);
        } else {
            throw new RuntimeException("Member not found");
        }
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public ResponseEntity<? super IdCheckResponseDTO> idcheck(IdCheckRequestDTO dto) {
        try {
            String memberId = dto.getMemberId();
            boolean isExistId = memberRepository.existsByMemberId(memberId);
            if (isExistId) {
                return IdCheckResponseDTO.duplicateId();
            } else {
                return IdCheckResponseDTO.success();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError();
        }
    }

    public ResponseEntity<? super EmailCertificationResponseDTO> emailCertification(EmailCertificationRequestDTO dto) {
        try {
            String memberId = dto.getMemberId();
            String email = dto.getMemberEmail();

            boolean isExistId = memberRepository.existsByMemberId(memberId);
            if (isExistId)
                return EmailCertificationResponseDTO.duplicateId();

            String certificationNumber = getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if (!isSuccessed)
                return EmailCertificationResponseDTO.mailSendFail();

            CertificationEntity certificationEntity = new CertificationEntity(memberId, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError();
        }
        return EmailCertificationResponseDTO.success();
    }

    private String getCertificationNumber() {
        StringBuilder certificationNumber = new StringBuilder();
        for (int count = 0; count < 6; count++)
            certificationNumber.append((int) (Math.random() * 10));
        return certificationNumber.toString();
    }

    public ResponseEntity<? super CheckCertificationResponseDTO> checkCertification(CheckCertificationRequestDTO dto) {
        try {
            String memberId = dto.getMemberId();
            String memberEmail = dto.getMemberEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByMemberId(memberId);
            if (certificationEntity == null)
                return CheckCertificationResponseDTO.certificationFail();

            boolean isMatched = certificationEntity.getMemberEmail().equals(memberEmail)
                    && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched)
                return CheckCertificationResponseDTO.certificationFail();

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError();
        }

        return CheckCertificationResponseDTO.success();
    }

    public ResponseEntity<? super SignUpResponseDTO> signUp(SignUpRequestDTO dto) {
        try {
            String memberId = dto.getMemberId();
            System.out.println("Sign Up Request for Member ID: " + memberId);

            boolean isExistId = memberRepository.existsByMemberId(memberId);
            if (isExistId) {
                System.out.println("Member ID already exists");
                return SignUpResponseDTO.duplicateId();
            }

            String memberEmail = dto.getMemberEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByMemberId(memberId);
            if (certificationEntity == null) {
                System.out.println("Certification entity not found for Member ID: " + memberId);
                return SignUpResponseDTO.certificationFail();
            }

            boolean isMatched = certificationEntity.getMemberEmail().equals(memberEmail)
                    && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) {
                System.out.println("Certification failed for Member ID: " + memberId);
                return SignUpResponseDTO.certificationFail();
            }

            String memberPw = dto.getMemberPw();
            String encodedPassword = passwordEncoder.encode(memberPw);
            System.out.println("Raw Password: " + memberPw);
            System.out.println("Encoded Password: " + encodedPassword);
            dto.setMemberPw(encodedPassword);

            MemberEntity memberEntity = new MemberEntity(dto);
            memberRepository.save(memberEntity);
            System.out.println("Member saved: " + memberEntity.getMemberId());

            certificationRepository.delete(certificationEntity);
            System.out.println("Certification entity deleted for Member ID: " + memberId);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError();
        }
        return SignUpResponseDTO.success();
    }

    // 아이디 찾기 기능
    public boolean sendIdToEmail(String email) {
        Optional<MemberEntity> member = memberRepository.findByMemberEmail(email);
        if (member.isPresent()) {
            String memberId = member.get().getMemberId();
            return emailProvider.sendIdMail(email, memberId);
        }
        return false;
    }

    // 비밀번호 재설정 기능
    public boolean sendPasswordResetLink(String memberId, String email) {
        Optional<MemberEntity> member = memberRepository.findByMemberIdAndMemberEmail(memberId, email);
        if (member.isPresent()) {
            String newPassword = emailProvider.generateRandomPassword();
            member.get().setMemberPw(passwordEncoder.encode(newPassword));
            memberRepository.save(member.get());
            return emailProvider.sendResetPasswordMail(email, newPassword);
        }
        return false;
    }
    //이메일 중복확인
    public boolean isEmailDuplicate(String memberEmail) {
        return memberRepository.existsByMemberEmail(memberEmail);
    }
}
