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

    @Value("${secret-key}")
    private String secretKey;  // JWT 토큰을 서명할 비밀 키

    // JWT 생성 메서드
    public String create(Long id, String memberId, String memberAuth) {
        // 토큰 만료 시간 설정 (1시간)
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        // 비밀 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // JWT 생성 및 반환
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)  // 서명 알고리즘 HS256 사용
                .setSubject(memberId)  // 사용자 ID 설정
                .claim("id", id)  // 사용자 고유 ID를 클레임에 저장
                .claim("auth", memberAuth)  // 사용자 권한을 클레임에 저장
                .setIssuedAt(new Date())  // 토큰 발행 시간 설정
                .setExpiration(expireDate)  // 토큰 만료 시간 설정
                .compact();  // 최종 토큰 생성
    }

    // JWT 유효성 검증 메서드
    public String validate(String jwt) {
        // 서명 검증을 위한 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            // 토큰을 파싱하여 유효성 검증 및 Claims 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            return claims.getSubject();  // Subject(사용자 ID) 반환

        } catch (Exception e) {
            System.out.println("Invalid or Expired JWT token.");
            return null;  // 토큰이 유효하지 않거나 만료된 경우 null 반환
        }
    }
}
