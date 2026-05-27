package project.boot.fideco.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import project.boot.fideco.dto.response.ResponseDTO;
import project.boot.fideco.common.ResponseCode;
import project.boot.fideco.common.ResponseMessage;

@Getter
public class IdCheckResponseDTO extends ResponseDTO {

	private IdCheckResponseDTO() {
		super();
	}

	public static ResponseEntity<IdCheckResponseDTO> success() {
		IdCheckResponseDTO responseBody = new IdCheckResponseDTO();
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}

	public static ResponseEntity<ResponseDTO> duplicateId() {

		ResponseDTO responseBody = new ResponseDTO(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}

}
