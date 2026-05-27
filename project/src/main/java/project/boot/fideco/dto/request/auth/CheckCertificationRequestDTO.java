package project.boot.fideco.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckCertificationRequestDTO {
	
	private String memberId;
	
	@Email
	@NotBlank
	private String memberEmail;
	
	@NotBlank
	private String certificationNumber;
	
}
