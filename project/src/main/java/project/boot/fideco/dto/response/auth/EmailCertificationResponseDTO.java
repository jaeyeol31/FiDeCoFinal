package project.boot.fideco.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import project.boot.fideco.dto.response.ResponseDTO;
import project.boot.fideco.common.ResponseCode;
import project.boot.fideco.common.ResponseMessage;

@Getter
public class EmailCertificationResponseDTO extends ResponseDTO {

    private EmailCertificationResponseDTO() {
        super();
    }

    // 인증 성공 시 응답을 반환하는 정적 메서드
    public static ResponseEntity<EmailCertificationResponseDTO> success() {
        EmailCertificationResponseDTO responseBody = new EmailCertificationResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    // 중복된 아이디일 경우 BAD_REQUEST 응답 반환
    public static ResponseEntity<ResponseDTO> duplicateId() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    // 이메일 발송 실패 시 INTERNAL_SERVER_ERROR 응답 반환
    public static ResponseEntity<ResponseDTO> mailSendFail() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
