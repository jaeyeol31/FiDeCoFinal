package project.boot.fideco.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.boot.fideco.dto.request.auth.CheckCertificationRequestDTO;
import project.boot.fideco.dto.request.auth.EmailCertificationRequestDTO;
import project.boot.fideco.dto.request.auth.IdCheckRequestDTO;
import project.boot.fideco.dto.request.auth.SignUpRequestDTO;
import project.boot.fideco.dto.response.auth.CheckCertificationResponseDTO;
import project.boot.fideco.dto.response.auth.EmailCertificationResponseDTO;
import project.boot.fideco.dto.response.auth.IdCheckResponseDTO;
import project.boot.fideco.dto.response.auth.SignUpResponseDTO;
import project.boot.fideco.member.service.MemberService;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;

	@PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDTO> idCheck(
            @RequestBody @Valid IdCheckRequestDTO requestBody) {
        return memberService.idcheck(requestBody);
    }

	@PostMapping("/email-certification")
	public ResponseEntity<? super EmailCertificationResponseDTO> emailCertification(
			
			@RequestBody @Valid EmailCertificationRequestDTO requestBody
			) {
			ResponseEntity<? super EmailCertificationResponseDTO> response = memberService.emailCertification(requestBody);

		return response;
	}
	
	@PostMapping("/check-certification")
	public ResponseEntity<? super CheckCertificationResponseDTO> checkCertification(
	
			@RequestBody @Valid CheckCertificationRequestDTO requestBody
			) {
			ResponseEntity<? super CheckCertificationResponseDTO> response = memberService.checkCertification(requestBody);

		return response;
	}
	@PostMapping("/signup")
	
	public ResponseEntity<? super SignUpResponseDTO> signUp(
			@RequestBody @Valid SignUpRequestDTO requestBody
			){
		ResponseEntity<? super SignUpResponseDTO> response = memberService.signUp(requestBody);
		return response;
	}
	
	

}
