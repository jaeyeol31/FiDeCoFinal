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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    // 요청마다 JWT 토큰을 확인하는 필터 처리 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 쿠키에서 JWT 토큰을 가져옴
            String token = getTokenFromCookie(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }
            // 토큰 유효성 검사 후 memberId를 반환
            String memberId = jwtProvider.validate(token);
            if (memberId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // memberId로 DB에서 사용자 정보 조회
            MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
            String auth = memberEntity.getMemberAuth(); // 권한 (ROLE_USER, ROLE_ADMIN)

            // 권한 리스트 생성
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(auth));  // 사용자의 권한을 GrantedAuthority로 변환

            // SecurityContext 생성 및 설정
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberId, null, authorities); // 인증 토큰 생성
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);  // SecurityContext에 인증 정보 설정

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        filterChain.doFilter(request, response); // 필터 체인에 다음 필터 실행
    }

    // 쿠키에서 JWT 토큰을 가져오는 메서드
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;  // 토큰이 없을 경우 null 반환
    }
}
