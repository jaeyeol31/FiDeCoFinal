package project.boot.fideco.login.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import project.boot.fideco.dto.response.ResponseDTO;
import project.boot.fideco.dto.request.auth.LogInRequestDTO;
import project.boot.fideco.dto.response.auth.LogInResponseDTO;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.repository.MemberRepository;
import project.boot.fideco.provider.JwtProvider;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtProvider jwtProvider;

    public ResponseEntity<? super LogInResponseDTO> logIn(LogInRequestDTO dto, HttpServletResponse response) {
        String token = null;
        try {
            String memberId = dto.getMemberId();
            MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
            if (memberEntity == null) return LogInResponseDTO.logInFail();

            String memberPw = dto.getMemberPw();
            String encodedPassword = memberEntity.getMemberPw();
            boolean isMatched = passwordEncoder.matches(memberPw, encodedPassword);
            if (!isMatched) return LogInResponseDTO.logInFail();

            token = jwtProvider.create(memberEntity.getId(), memberEntity.getMemberId(), memberEntity.getMemberAuth());


            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setHttpOnly(false); // 자바스크립트에서 접근하지 못하게 설정
            cookie.setSecure(false); // HTTPS에서만 전송
            cookie.setPath("/");
            response.addCookie(cookie);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError();
        }
        return LogInResponseDTO.success(token);
    }
}

