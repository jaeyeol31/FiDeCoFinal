package project.boot.fideco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import project.boot.fideco.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
		// CORS 설정과 함께 보안 필터 체인을 구성
		httpSecurity
				//현재는 타임리프만 사용으로 설정 x
//				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				// CSRF 보안 비활성화(JWT 사용)
				.csrf(csrf -> csrf.disable())

				// 세션을 상태를 유지하지 않음 (JWT 사용 시 필요)
				.sessionManagement(
						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// 권한에 따른 접근 설정
				.authorizeHttpRequests(request -> request
						.requestMatchers("/NoticeSelect", "/NoticeSelectDetail", "/NoticeSearch","/products/compareProducts/**","/products/productView/**","/products/productOrder/**").permitAll() // 공용
						.requestMatchers("/products/**", "/member/memberList", "/cart/list", "/orders/list", "/orders/update").hasRole("ADMIN") // 관리자
						.anyRequest().permitAll()) // 그 외 요청은 모두 허용 -> 사용자
				// 접근 권한이 없을 시 이동할 페이지 설정
				.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/access-denied"))

				// OAuth2 로그인 설정
				.oauth2Login(oauth2 -> oauth2.redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/*")))

				// JWT 필터 추가
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build(); // 보안 필터 체인을 반환
	}

	// CORS 설정을 위한 메서드 -> 사용안함
//	@Bean
//	protected CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.addAllowedOriginPattern("*"); // 모든 도메인 허용
//		corsConfiguration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
//		corsConfiguration.addAllowedHeader("*"); // 모든 헤더 허용
//		corsConfiguration.setAllowCredentials(true); // 쿠키 사용 허용
//		corsConfiguration.addExposedHeader("Authorization"); // Authorization 헤더 노출
//
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", corsConfiguration); // 모든 경로에 대해 CORS 설정 적용
//
//		return source; // CORS 설정 반환
//	}
}
