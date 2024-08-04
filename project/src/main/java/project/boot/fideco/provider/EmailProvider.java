//package project.boot.fideco.provider;
//
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//
//@Component
//@RequiredArgsConstructor
//public class EmailProvider {
//
//		private final JavaMailSender javaMailSender;
//		
//		private final String SUBJECT = "[웹사이트] 인증 메일입니다.";
//		
//		public boolean sendCertificationMail(String email,String CertificationNumber) {
//			
//			
//			try {
//			
//				MimeMessage message = javaMailSender.createMimeMessage();
//				MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
//				
//				String htmlContent = getCertificationMessge(CertificationNumber);
//				
//				messageHelper.setTo(email);
//				messageHelper.setSubject(SUBJECT);
//				messageHelper.setText(htmlContent, true);
//				
//				javaMailSender.send(message);		
//				
//				
//			} catch (Exception exception) {
//				exception.printStackTrace();
//				return false;
//			}
//			return true;
//		}
//		
//		private String getCertificationMessge(String certificationNumber) {
//			
//			String certificationMessage = "";
//			certificationMessage += "<h1 style='text-align: center;'> [웹사이트] 인증메일</h1>";
//			certificationMessage += "<h3 style='text-align: center;'> 인증코드 : <strong style = 'font-sizei : 32px; letter-spacing: 8px;'>"+certificationNumber+"</strong></h3>";
//			return certificationMessage;
//			
//		}
//}
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

    private final JavaMailSender javaMailSender;

    private final String CERTIFICATION_SUBJECT = "[피데코] 인증 메일입니다.";
    private final String FIND_ID_SUBJECT = "[피데코] 회원 아이디 안내입니다.";
    private final String RESET_PASSWORD_SUBJECT = "[피데코] 비밀번호 재설정 안내입니다.";

    public boolean sendCertificationMail(String email, String certificationNumber) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            String htmlContent = getCertificationMessage(certificationNumber);

            messageHelper.setTo(email);
            messageHelper.setSubject(CERTIFICATION_SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);        

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

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

    private String getCertificationMessage(String certificationNumber) {
        return "<h1 style='text-align: center;'> [피데코] 인증메일</h1>" +
               "<h3 style='text-align: center;'> 인증코드 : <strong style='font-size: 32px; letter-spacing: 8px;'>" + certificationNumber + "</strong></h3>";
    }

    private String getIdMessage(String memberId) {
        return "<h1 style='text-align: center;'> [피데코] 회원 아이디 안내</h1>" +
               "<h3 style='text-align: center;'> 회원 아이디 : <strong style='font-size: 32px; letter-spacing: 8px;'>" + memberId + "</strong></h3>";
    }

    private String getResetPasswordMessage(String newPassword) {
        return "<h1 style='text-align: center;'> [피데코] 비밀번호 재설정 안내</h1>" +
               "<p style='text-align: center;'> 새로운 비밀번호는 다음과 같습니다:</p>" +
               "<p style='text-align: center; font-size: 24px; font-weight: bold;'>" + newPassword + "</p>";
    }

    public String generateRandomPassword() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
