package project.boot.fideco.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import project.boot.fideco.dto.response.ResponseDTO;
import project.boot.fideco.common.ResponseCode;
import project.boot.fideco.common.ResponseMessage;

@Getter
public class LogInResponseDTO extends ResponseDTO {

    private String token; // 로그인 성공 시 발급되는 JWT 토큰
    private int expirationTime; // 토큰의 만료 시간 (3600초 = 1시간)

    // 생성자: 로그인 성공 시 토큰과 만료 시간을 설정
    private LogInResponseDTO(String token) {
        super();
        this.token = token;
        this.expirationTime = 3600; // 기본 만료 시간 3600초 설정
    }

    // 로그인 성공 시 응답으로 반환할 ResponseEntity를 생성
    public static ResponseEntity<LogInResponseDTO> success(String token) {
        LogInResponseDTO responseBody = new LogInResponseDTO(token);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody); // HTTP 상태 200(OK)와 함께 응답
    }

    // 로그인 실패 시 응답으로 반환할 ResponseEntity를 생성
    public static ResponseEntity<ResponseDTO> logInFail() {
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody); // HTTP 상태 401(Unauthorized)와 함께 응답
    }
}
