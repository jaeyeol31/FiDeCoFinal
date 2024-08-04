package project.boot.fideco.dto.request.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDTO {

	@NotBlank(message = "회원 아이디는 필수 입력 항목입니다.")
	// 회원 아이디는 필수 입력 항목입니다.
	private String memberId;

	@NotBlank(message = "회원 이름은 필수 입력 항목입니다.")
	// 회원 이름은 필수 입력 항목입니다.
	private String memberName;

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Pattern(regexp ="^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}+$",
	message = "비밀번호는 대소문자 + 숫자 + 특수문자 조합으로 8 ~ 20자리이어야 합니다..")
	// 비밀번호는 필수 입력 항목이며, 8~20자 길이의 대문자, 숫자, 특수문자를 포함해야 합니다.
	private String memberPw;

	@NotBlank(message = "회원 주소는 필수 입력 항목입니다.")
	// 회원 주소는 필수 입력 항목입니다.
	private String memberAddress;

	@NotBlank(message = "전화번호는 필수 입력 항목입니다.")
	// 전화번호는 필수 입력 항목입니다.
	private String memberPhone;

	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	// 이메일은 필수 입력 항목이며, 유효한 이메일 형식이어야 합니다.
	private String memberEmail;

	@NotBlank(message = "인증 번호는 필수 입력 항목입니다.")
	// 인증 번호는 필수 입력 항목입니다.
	private String certificationNumber;
}
