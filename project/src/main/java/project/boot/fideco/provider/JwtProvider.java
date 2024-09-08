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
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

    @Value("${secret-key}")
    private String secretKey;

    // JWT 생성 메서드
    public String create(Long id, String memberId, String memberAuth) {
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));  // 만료시간 1시간
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)  // 보안 강화된 알고리즘 사용
                .setSubject(memberId)  // 사용자 ID 저장
                .claim("id", id)  // 사용자 고유 ID 저장
                .claim("auth", memberAuth)  // 사용자 권한 저장
                .setIssuedAt(new Date())  // 발행일자 설정
                .setExpiration(expireDate)  // 만료일자 설정
                .compact();
    }

    // JWT 유효성 검증 메서드
    public String validate(String jwt) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)  // JWT를 파싱 및 유효성 검증
                    .getBody();  // 유효한 경우 Claims 반환

            return claims.getSubject();  // Subject를 반환하여 String 타입 반환
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT signature.");
            return null;
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token.");
            return null;
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token.");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT token compact of handler are invalid.");
            return null;
        }
    }
}
