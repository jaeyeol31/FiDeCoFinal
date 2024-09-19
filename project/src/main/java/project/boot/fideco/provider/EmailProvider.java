package project.boot.fideco.provider;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class EmailProvider {

    // 스프링에서 제공하는 이메일 발송 클래스
    private final JavaMailSender javaMailSender;

    // 이메일 제목 상수
    private final String CERTIFICATION_SUBJECT = "[피데코] 인증 메일입니다.";
    private final String FIND_ID_SUBJECT = "[피데코] 회원 아이디 안내입니다.";
    private final String RESET_PASSWORD_SUBJECT = "[피데코] 비밀번호 재설정 안내입니다.";

    // 이메일 인증 코드 발송 메서드
    public boolean sendCertificationMail(String email, String certificationNumber) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();  // 이메일 메시지 객체 생성
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String htmlContent = getCertificationMessage(certificationNumber);  // 인증 메시지 내용 생성
            messageHelper.setTo(email);  // 수신자 설정
            messageHelper.setSubject(CERTIFICATION_SUBJECT);  // 이메일 제목 설정
            messageHelper.setText(htmlContent, true);  // HTML 형식의 내용 설정

            javaMailSender.send(message);  // 이메일 발송
        } catch (Exception exception) {
            exception.printStackTrace();
            return false; 
        }
        return true;  
    }

    // 아이디 안내 메일 발송 메서드
    public boolean sendIdMail(String email, String memberId) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String htmlContent = getIdMessage(memberId);
            messageHelper.setTo(email);
            messageHelper.setSubject(FIND_ID_SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    // 비밀번호 재설정 안내 메일 발송 메서드
    public boolean sendResetPasswordMail(String email, String newPassword) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String htmlContent = getResetPasswordMessage(newPassword);
            messageHelper.setTo(email);
            messageHelper.setSubject(RESET_PASSWORD_SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    // 인증 메일 내용 생성
    private String getCertificationMessage(String certificationNumber) {
        return "<h1 style='text-align: center;'> [피데코] 인증메일</h1>" +
               "<h3 style='text-align: center;'> 인증코드 : <strong style='font-size: 32px; letter-spacing: 8px;'>" + certificationNumber + "</strong></h3>";
    }

    // 회원 아이디 안내 메일 내용 생성
    private String getIdMessage(String memberId) {
        return "<h1 style='text-align: center;'> [피데코] 회원 아이디 안내</h1>" +
               "<h3 style='text-align: center;'> 회원 아이디 : <strong style='font-size: 32px; letter-spacing: 8px;'>" + memberId + "</strong></h3>";
    }

    // 비밀번호 재설정 안내 메일 내용 생성
    private String getResetPasswordMessage(String newPassword) {
        return "<h1 style='text-align: center;'> [피데코] 비밀번호 재설정 안내</h1>" +
               "<p style='text-align: center;'> 새로운 비밀번호는 다음과 같습니다:</p>" +
               "<p style='text-align: center; font-size: 24px; font-weight: bold;'>" + newPassword + "</p>";
    }

    // 임의의 비밀번호 생성 메서드
    public String generateRandomPassword() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // 8자리의 임의 비밀번호 생성
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();  // 생성된 비밀번호 반환
    }
}
