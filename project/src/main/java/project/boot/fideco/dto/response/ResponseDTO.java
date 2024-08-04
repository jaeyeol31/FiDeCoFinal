package project.boot.fideco.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.boot.fideco.common.ResponseCode;
import project.boot.fideco.common.ResponseMessage;

@Getter
@AllArgsConstructor
public class ResponseDTO {
	private String code;
	private String message;

	public ResponseDTO() {

		this.code = ResponseCode.SUCCESS;
		this.message = ResponseMessage.SUCCESS;
	}

	public static ResponseEntity<ResponseDTO> databaseError() {
		ResponseDTO responseBody = new ResponseDTO(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
	}
}
