//package project.boot.fideco.provider;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//@Component
//public class JwtProvider {
//
//    @Value("${secret-key}")
//    private String secretKey;
//
//    public String create(Long id, String memberId, String memberAuth) {
//        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
//        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//
//        return Jwts.builder()
//                .signWith(key, SignatureAlgorithm.HS256)
//                .setSubject(memberId)
//                .claim("id", id)
//                .claim("auth", memberAuth) // 권한을 클레임에 추가
//                .setIssuedAt(new Date())
//                .setExpiration(expireDate)
//                .compact();
//        }
//
//    public String validate(String jwt) {
//        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build() // parserBuilder() 이후 build()를 호출해야 합니다.
//                    .parseClaimsJws(jwt)
//                    .getBody();
//
//            return claims.getSubject();
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            return null;
//        }
//    }
//}
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
    private String secretKey;

    public String create(Long id, String memberId, String memberAuth) {
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(memberId)
                .claim("id", id)
                .claim("auth", memberAuth) // 권한을 클레임에 추가
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();
        }

    public String validate(String jwt) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            return claims.getSubject();
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
