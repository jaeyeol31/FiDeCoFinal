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
    private JwtProvider jwtProvider;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        Map<String, Object> attributes = (Map<String, Object>) oAuth2User.getAttributes().get("response");
        String naverId = (String) attributes.get("id");
        String email = (String) attributes.get("email");
        String phone = (String) attributes.get("mobile");

        MemberEntity member = memberRepository.findByMemberEmail(email).orElse(null);

        if (member == null) {
            member = new MemberEntity();
            member.setMemberId("naver_" + naverId);
            member.setMemberEmail(email);
            member.setMemberPhone(phone);
            member.setMemberAuth("ROLE_USER");
            member.setMemberType("naver");

            memberRepository.save(member);

            Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, Collections.singletonList(new SimpleGrantedAuthority(member.getMemberAuth())));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String redirectUrl = UriComponentsBuilder.fromPath("/member/update")
                    .queryParam("memberId", member.getMemberId())
                    .queryParam("memberEmail", member.getMemberEmail())
                    .queryParam("memberPhone", member.getMemberPhone())
                    .toUriString();
            try {
                httpServletResponse.sendRedirect(redirectUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return oAuth2User;
        }

        String jwtToken = jwtProvider.create(member.getId(), member.getMemberId(), member.getMemberAuth());
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setHttpOnly(false); // 자바스크립트에서 접근하지 못하게 설정
        cookie.setSecure(false); // HTTPS에서만 전송
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return oAuth2User;
    }
}
