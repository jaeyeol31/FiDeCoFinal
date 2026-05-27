package project.boot.fideco.login.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import project.boot.fideco.dto.request.auth.LogInRequestDTO;
import project.boot.fideco.dto.response.auth.LogInResponseDTO;
import project.boot.fideco.login.service.CustomOAuth2UserService;
import project.boot.fideco.login.service.LoginService;
import project.boot.fideco.member.entity.MemberEntity;
import project.boot.fideco.member.repository.MemberRepository;
import project.boot.fideco.member.service.MemberService;
import project.boot.fideco.provider.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.Map;

@Controller
public class LoginController {

    private final LoginService loginService;
    //    private final MemberService memberService;
    private final CustomOAuth2UserService customOAuth2UserService;

    // 생성자 주입 방식으로 서비스 객체들 주입
    @Autowired
    public LoginController(LoginService loginService, CustomOAuth2UserService customOAuth2UserService) {
        this.loginService = loginService;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    // 로그인 폼 페이지를 반환
    @GetMapping("/log-in")
    public String loginForm(Model model) {
        model.addAttribute("memberEntity", new MemberEntity()); // 템플릿에 새로운 MemberEntity 객체 전달
        return "login"; // login 템플릿 페이지 반환
    }

    // 로그인 처리
    @PostMapping("/log-in")
    public ResponseEntity<? super LogInResponseDTO> logIn(@RequestBody @Valid LogInRequestDTO requestBody, HttpServletResponse response) {
        // 로그인 서비스에서 로그인을 처리하고, ResponseEntity로 응답
        ResponseEntity<? super LogInResponseDTO> responseEntity = loginService.logIn(requestBody, response);
        return responseEntity; // 로그인 성공 또는 실패 시 ResponseEntity 반환
    }

    // 로그아웃 처리
    @GetMapping("/log-out")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // SecurityContext에서 인증된 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // 인증된 사용자가 있을 경우 로그아웃 처리
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/index"; // 로그아웃 후 메인 페이지로 리다이렉션
    }

    // 카카오 OAuth2 로그인 처리
    @GetMapping("/log-in/oauth/kakao")
    public String loginKakao(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2User oAuth2User = authentication.getPrincipal(); // OAuth2 인증된 사용자 정보 가져오기
        model.addAttribute("name", oAuth2User.getAttribute("properties.nickname")); // 카카오에서 닉네임 가져오기
        return "redirect:/index"; // 메인 페이지로 리다이렉션
    }

    @GetMapping("/login/oauth2/code/naver")
    public String loginNaver(OAuth2AuthenticationToken authentication) {
        customOAuth2UserService.processNaverLogin(authentication.getPrincipal());
        return "redirect:/";
    }
}
