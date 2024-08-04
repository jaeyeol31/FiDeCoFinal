package project.boot.fideco.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import project.boot.fideco.dto.response.ResponseDTO;
import project.boot.fideco.common.ResponseCode;
import project.boot.fideco.common.ResponseMessage;

@Getter
public class LogInResponseDTO extends ResponseDTO {

	private String token;
	private int expirationTime;

	private LogInResponseDTO(String token) {
		super();
		this.token = token;
		this.expirationTime = 3600;

	}

	public static ResponseEntity<LogInResponseDTO> success(String token) {
		LogInResponseDTO responseBody = new LogInResponseDTO(token);
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}

	public static ResponseEntity<ResponseDTO> logInFail() {
		ResponseDTO responseBody = new ResponseDTO(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);

	}
	
	
}
