package project.boot.fideco.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import project.boot.fideco.dto.response.ResponseDTO;

@Getter
public class CheckCertificationResponseDTO extends ResponseDTO{

	private CheckCertificationResponseDTO() {
		super();
	}
	
	public static ResponseEntity<CheckCertificationResponseDTO> success(){
		
		CheckCertificationResponseDTO responseBody = new CheckCertificationResponseDTO();
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
		
	}
	
public static ResponseEntity<ResponseDTO> certificationFail(){
		
		ResponseDTO responseBody = new ResponseDTO();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
		
	}
	
	
}
