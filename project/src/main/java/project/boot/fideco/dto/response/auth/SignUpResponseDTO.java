package project.boot.fideco.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import project.boot.fideco.dto.response.ResponseDTO;
import project.boot.fideco.common.ResponseCode;
import project.boot.fideco.common.ResponseMessage;

@Getter
public class SignUpResponseDTO extends ResponseDTO{

	private SignUpResponseDTO() {
		super();
	}

	public static ResponseEntity<SignUpResponseDTO> success() {
		SignUpResponseDTO responseBody = new SignUpResponseDTO();
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}
	
	public static ResponseEntity<ResponseDTO> duplicateId() {
		ResponseDTO responseBody = new ResponseDTO(ResponseCode.DUPLICATE_ID,ResponseMessage.DUPLICATE_ID);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}
	
	public static ResponseEntity<ResponseDTO> certificationFail() {
		ResponseDTO responseBody = new ResponseDTO(ResponseCode.DUPLICATE_ID,ResponseMessage.DUPLICATE_ID);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
	}
	
}
