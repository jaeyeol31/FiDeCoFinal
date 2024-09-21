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
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.repository.MemberRepository;
import project.boot.fideco.provider.JwtProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private JwtProvider jwtProvider; // JWT 생성 및 검증을 위한 프로바이더

    @Autowired
    private HttpServletResponse httpServletResponse; // HTTP 응답을 위한 객체

    @Autowired
    private MemberRepository memberRepository; // 사용자 정보를 저장하고 조회하는 레포지토리

    // OAuth2 로그인 시 사용자 정보를 로드하는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest); // OAuth2 제공자로부터 사용자 정보 받아오기

        // 네이버 OAuth2에서는 사용자 정보가 response라는 키로 제공됨
        Map<String, Object> attributes = (Map<String, Object>) oAuth2User.getAttributes().get("response");
        String naverId = (String) attributes.get("id"); // 네이버 ID
        String email = (String) attributes.get("email"); // 이메일 정보
        String phone = (String) attributes.get("mobile"); // 전화번호 정보

        // 이메일로 기존 회원 조회
        MemberEntity member = memberRepository.findByMemberEmail(email).orElse(null);

        // 회원이 없거나 이름 정보가 없는 경우 회원가입 페이지로 리다이렉션
        if (member == null || member.getMemberName() == null) {
            if (member == null) {
                // 회원 정보가 없으면 새로운 회원 정보 생성
                member = new MemberEntity();
                member.setMemberId("naver_" + naverId); // 네이버 ID로 설정
                member.setMemberEmail(email);
                member.setMemberPhone(phone);
                member.setMemberAuth("ROLE_USER"); // 기본 권한을 ROLE_USER로 설정
                member.setMemberType("naver"); // 네이버 로그인 방식으로 설정
                memberRepository.save(member); // 새 회원 정보 저장
            }

            // 회원가입 페이지로 리다이렉션
            String redirectUrl = "/member/update/" + member.getId();  
            try {
                httpServletResponse.sendRedirect(redirectUrl); // 회원 정보 수정 페이지로 리다이렉션
            } catch (IOException e) {
                e.printStackTrace();
            }
            return oAuth2User; // oAuth2User를 반환하고 로직 종료
        }

        // 회원이 존재하는 경우 JWT 생성
        String jwtToken = jwtProvider.create(member.getId(), member.getMemberId(), member.getMemberAuth());
        Cookie cookie = new Cookie("jwtToken", jwtToken); // JWT 토큰을 쿠키에 저장
        cookie.setHttpOnly(false); // 클라이언트에서 접근 가능
        cookie.setSecure(false); // HTTPS에서만 전송 설정 (false로 설정되어 있음)
        cookie.setPath("/"); // 모든 경로에 대해 쿠키 전송
        httpServletResponse.addCookie(cookie); // 쿠키에 JWT 추가

        return oAuth2User; // OAuth2User 반환
    }
}
