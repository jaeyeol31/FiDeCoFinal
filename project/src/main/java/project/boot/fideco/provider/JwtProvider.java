package project.boot.fideco.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${secret-key}") // 애플리케이션 설정 파일에서 "secret-key" 값을 주입받음
    private String secretKey;  // JWT 토큰을 서명할 비밀 키

    // JWT 생성 메서드
    public String create(Long id, String memberId, String memberAuth) {
        // 토큰 만료 시간 설정 (1시간)
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        // 비밀 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // JWT 생성 및 반환
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)  // 서명 알고리즘 HS256 사용, 키를 이용하여 서명 설정
                .setSubject(memberId)  // 사용자 ID 설정 (토큰의 주체로 사용)
                .claim("id", id)  // 사용자 고유 ID를 클레임에 저장
                .claim("auth", memberAuth)  // 사용자 권한을 클레임에 저장
                .setIssuedAt(new Date())  // 토큰 발행 시간 설정 (현재 시간)
                .setExpiration(expireDate)  // 토큰 만료 시간 설정 (1시간 뒤)
                .compact();  // 최종적으로 JWT 토큰을 생성하여 반환
    }

    // JWT 유효성 검증 메서드
    public String validate(String jwt) {
        // 서명 검증을 위한 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            // 토큰을 파싱하여 유효성 검증 및 Claims 추출
            Claims claims = Jwts.parserBuilder() // JWT 파서를 생성
                    .setSigningKey(key) // 서명 키 설정
                    .build() // 파서 빌드
                    .parseClaimsJws(jwt) // JWT 토큰을 파싱하여 유효성 검증 수행
                    .getBody(); // 토큰의 클레임 부분 추출

            return claims.getSubject();  // Subject(사용자 ID)를 반환

        } catch (Exception e) { // 유효하지 않거나 만료된 토큰일 경우 예외 처리
            System.out.println("Invalid or Expired JWT token."); // 유효하지 않은 토큰 경고 출력
            return null;  // 토큰이 유효하지 않거나 만료된 경우 null 반환
        }
    }
}