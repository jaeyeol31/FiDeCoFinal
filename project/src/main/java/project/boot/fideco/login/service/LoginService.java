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

    private final MemberRepository memberRepository; // 회원 레포지토리
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 엔코더
    private final JwtProvider jwtProvider; // JWT 발급을 위한 프로바이더

    // 로그인 처리 로직
    public ResponseEntity<? super LogInResponseDTO> logIn(LogInRequestDTO dto, HttpServletResponse response) {
        String token = null;
        try {
            // 입력받은 회원 ID로 회원 조회
            String memberId = dto.getMemberId();
            MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
            if (memberEntity == null) return LogInResponseDTO.logInFail(); // 회원이 없으면 실패 응답

            // 입력받은 비밀번호와 데이터베이스에 저장된 비밀번호 비교
            String memberPw = dto.getMemberPw();
            String encodedPassword = memberEntity.getMemberPw();
            boolean isMatched = passwordEncoder.matches(memberPw, encodedPassword);
            if (!isMatched) return LogInResponseDTO.logInFail(); // 비밀번호가 일치하지 않으면 실패 응답

            // 비밀번호가 일치하면 JWT 토큰 생성
            token = jwtProvider.create(memberEntity.getId(), memberEntity.getMemberId(), memberEntity.getMemberAuth());

            // JWT 토큰을 쿠키에 저장
            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setHttpOnly(false); // HTTP 전송만 가능하도록 설정
            cookie.setSecure(false); // HTTPS에서만 전송되도록 설정
            cookie.setPath("/");
            response.addCookie(cookie); // 쿠키 추가

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDTO.databaseError(); // 예외 발생 시 데이터베이스 오류 응답
        }
        return LogInResponseDTO.success(token); // 성공 시 JWT 토큰과 함께 응답
    }
}
