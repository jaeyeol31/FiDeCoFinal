package project.boot.fideco.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.repository.MemberRepository;
import project.boot.fideco.provider.JwtProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private JwtProvider jwtProvider; // JWT 생성을 위한 JwtProvider 의존성 주입

    @Autowired
    private HttpServletResponse httpServletResponse; // HTTP 응답 처리를 위한 HttpServletResponse 의존성 주입

    @Autowired
    private MemberRepository memberRepository; // 회원 정보를 관리하기 위한 MemberRepository 의존성 주입

    /**
     * 네이버 로그인 처리를 담당하는 메서드.
     * 네이버로부터 받은 사용자 정보를 통해 회원가입을 진행하거나 JWT를 발급.
     * @param oAuth2User OAuth2 제공자로부터 받은 사용자 정보
     * @return OAuth2User 인증된 사용자 정보 반환
     */
    public OAuth2User processNaverLogin(OAuth2User oAuth2User) {
        // 네이버 사용자 정보 가져오기
        Map<String, Object> attributes = (Map<String, Object>) oAuth2User.getAttributes().get("response");
        String naverId = (String) attributes.get("id");
        String email = (String) attributes.get("email");
        String phone = (String) attributes.get("mobile");

        // 이메일을 이용해 기존 회원 조회
        MemberEntity member = memberRepository.findByMemberEmail(email).orElse(null);

        // 회원이 없거나 이름 정보가 없는 경우 회원가입 페이지로 리다이렉션
        if (member == null || member.getMemberName() == null) {
            if (member == null) {
                // 회원 정보가 없으면 새로운 회원 정보 생성 및 저장
                member = new MemberEntity();
                member.setMemberId("naver_" + naverId);
                member.setMemberEmail(email);
                member.setMemberPhone(phone);
                member.setMemberAuth("ROLE_USER");
                member.setMemberType("naver");
                memberRepository.save(member);
            }

            // 회원가입 페이지로 리다이렉션하기 전에 JWT 발급 및 SecurityContext 설정
            String jwtToken = jwtProvider.create(member.getId(), member.getMemberId(), member.getMemberAuth());
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(false); // HTTP 전송만 가능하도록 설정
            cookie.setSecure(false); // HTTPS에서만 전송 설정 (false로 설정)
            cookie.setPath("/"); // 모든 경로에 대해 쿠키 전송
            httpServletResponse.addCookie(cookie); // 쿠키에 JWT 추가

            // 인증 정보 설정 (로그인 상태 유지)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    member, null, List.of(new SimpleGrantedAuthority(member.getMemberAuth())));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 회원가입 페이지로 리다이렉션
            String redirectUrl = "/member/update/" + member.getId();
            try {
                httpServletResponse.sendRedirect(redirectUrl); // 리다이렉션 처리
            } catch (IOException e) {
                e.printStackTrace();
            }
            return oAuth2User; // 회원가입이 필요한 경우 oAuth2User 반환
        }

        // 기존 회원인 경우 JWT 생성 및 쿠키에 추가
        logger.info("기존 회원 확인 - JWT 생성 중...");
        String jwtToken = jwtProvider.create(member.getId(), member.getMemberId(), member.getMemberAuth());
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(false); // HTTP 전송만 가능하도록 설정
        cookie.setSecure(false); // HTTPS에서만 전송 설정 (false로 설정)
        cookie.setPath("/"); // 모든 경로에 대해 쿠키 전송
        httpServletResponse.addCookie(cookie); // 쿠키에 JWT 추가
        logger.info("JWT 토큰 생성 및 쿠키에 추가 - 토큰: {}", jwtToken);

        return oAuth2User; // OAuth2User 반환 (인증 완료)
    }

    /**
     * OAuth2 사용자 정보를 로드하는 메서드.
     * 네이버 로그인을 처리하기 위해 processNaverLogin 메서드를 호출.
     * @param userRequest OAuth2UserRequest 사용자 요청 정보
     * @return OAuth2User 인증된 사용자 정보 반환
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 기본 OAuth2 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 네이버 로그인 처리
        return processNaverLogin(oAuth2User);
    }
}
