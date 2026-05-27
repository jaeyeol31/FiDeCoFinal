package project.boot.fideco.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.repository.MemberRepository;
import project.boot.fideco.provider.JwtProvider;

@Component // Spring Bean으로 등록하기 위한 어노테이션
@RequiredArgsConstructor // 필수 필드를 생성자를 통해 자동으로 주입하기 위한 Lombok 어노테이션
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository; // 회원 정보 저장소
    private final JwtProvider jwtProvider; // JWT 토큰을 생성 및 검증하는 클래스

    // 요청마다 JWT 토큰을 확인하는 필터 처리 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // 쿠키에서 JWT 토큰을 가져옴
            String token = getTokenFromCookie(request); // HTTP 요청에서 쿠키를 가져와 JWT 토큰을 찾음
            if (token == null) { // JWT 토큰이 없는 경우
                filterChain.doFilter(request, response); // 다음 필터로 요청을 전달하고 현재 필터를 종료
                return;
            }
            // 토큰 유효성 검사 후 memberId를 반환
            String memberId = jwtProvider.validate(token); // JWT 토큰의 유효성을 검사하고 회원 ID를 가져옴
            if (memberId == null) { // 토큰이 유효하지 않으면
                filterChain.doFilter(request, response); // 다음 필터로 요청을 전달하고 현재 필터를 종료
                return;
            }

            // memberId로 DB에서 사용자 정보 조회
            MemberEntity memberEntity = memberRepository.findByMemberId(memberId); // 회원 ID를 통해 데이터베이스에서 회원 정보 조회
            String auth = memberEntity.getMemberAuth(); // 권한 (ROLE_USER, ROLE_ADMIN) 가져오기

            // 권한 리스트 생성
            List<GrantedAuthority> authorities = new ArrayList<>(); // 권한 정보를 담을 리스트 생성
            authorities.add(new SimpleGrantedAuthority(auth));  // 사용자의 권한을 GrantedAuthority 객체로 변환하여 리스트에 추가

            // SecurityContext 생성 및 설정
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // 빈 SecurityContext 객체 생성
            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, null, authorities); // 인증 토큰 생성 (주요정보,  자격증명, 권한)
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 요청 정보와 관련된 인증 세부 정보 설정
            securityContext.setAuthentication(authenticationToken); // SecurityContext에 인증 정보 설정
            SecurityContextHolder.setContext(securityContext);  // 현재 요청의 SecurityContextHolder에 설정된 SecurityContext 저장

        } catch (Exception exception) { // 예외 처리
            exception.printStackTrace(); // 예외가 발생하면 스택 트레이스를 출력
        }

        filterChain.doFilter(request, response); // 필터 체인에 다음 필터 실행
    }

    // 쿠키에서 JWT 토큰을 가져오는 메서드
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies(); // 요청에서 쿠키 배열을 가져옴
        if (cookies != null) { // 쿠키가 존재하는 경우
            for (Cookie cookie : cookies) { // 쿠키 배열을 순회하면서
                if ("jwtToken".equals(cookie.getName())) { // 쿠키의 이름이 "jwtToken"인 경우
                    return cookie.getValue(); // 해당 쿠키의 값을 반환 (JWT 토큰)
                }
            }
        }
        return null;  // 토큰이 없을 경우 null 반환
    }
}
