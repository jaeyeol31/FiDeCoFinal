////package project.boot.fideco.filter;
////
////import java.io.IOException;
////import java.util.ArrayList;
////import java.util.List;
////
////import org.springframework.security.authentication.AbstractAuthenticationToken;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.context.SecurityContext;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
////import org.springframework.stereotype.Component;
////import org.springframework.util.StringUtils;
////import org.springframework.web.filter.OncePerRequestFilter;
////
////import jakarta.servlet.FilterChain;
////import jakarta.servlet.ServletException;
////import jakarta.servlet.http.Cookie;
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.servlet.http.HttpServletResponse;
////import lombok.RequiredArgsConstructor;
////import project.boot.fideco.member.entity.MemberEntity;
////import project.boot.fideco.member.repository.MemberRepository;
////import project.boot.fideco.provider.JwtProvider;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////
////@Component
////@RequiredArgsConstructor
////public class JwtAuthenticationFilter extends OncePerRequestFilter {
////	private final MemberRepository memberRepository;
////	private final JwtProvider jwtProvider;
////
////	@Override
////	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
////	        throws ServletException, IOException {
////
////	    try {
////	        String token = getTokenFromCookie(request);
////	        if (token == null) {
////	            filterChain.doFilter(request, response);
////	            return;
////	        }
////	        String memberId = jwtProvider.validate(token);
////	        if (memberId == null) {
////	            filterChain.doFilter(request, response);
////	            return;
////	        }
////
////	        MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
////	        String auth = memberEntity.getMemberAuth(); // role : ROLE_USER, ROLE_ADMIN
////
////	        List<GrantedAuthority> authorities = new ArrayList<>();
////	        authorities.add(new SimpleGrantedAuthority(auth));
////
////	        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
////
////	        AbstractAuthenticationToken authenticationToken =
////	                new UsernamePasswordAuthenticationToken(memberId, null, authorities);
////
////	        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////
////	        securityContext.setAuthentication(authenticationToken);
////	        SecurityContextHolder.setContext(securityContext);
////
////	    } catch (Exception exception) {
////	        exception.printStackTrace();
////	    }
////
////	    filterChain.doFilter(request, response);
////	}
////
////	private String getTokenFromCookie(HttpServletRequest request) {
////	    Cookie[] cookies = request.getCookies();
////	    if (cookies != null) {
////	        for (Cookie cookie : cookies) {
////	            if ("jwtToken".equals(cookie.getName())) {
////	                return cookie.getValue();
////	            }
////	        }
////	    }
////	    return null;
////	}
////}
//package project.boot.fideco.filter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import project.boot.fideco.member.entity.MemberEntity;
//import project.boot.fideco.member.repository.MemberRepository;
//import project.boot.fideco.provider.JwtProvider;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final MemberRepository memberRepository;
//    private final JwtProvider jwtProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        try {
//            String token = getTokenFromCookie(request);
//            if (token == null) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//            String memberId = jwtProvider.validate(token);
//            if (memberId == null) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
//            String auth = memberEntity.getMemberAuth(); // role : ROLE_USER, ROLE_ADMIN
//
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority(auth));
//
//            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//
//            AbstractAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(memberId, null, authorities);
//
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            securityContext.setAuthentication(authenticationToken);
//            SecurityContextHolder.setContext(securityContext);
//
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String getTokenFromCookie(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("jwtToken".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//}
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = getTokenFromCookie(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }
            String memberId = jwtProvider.validate(token);
            if (memberId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
            if (memberEntity == null) {
                response.sendRedirect("/member/additional-info");
                return;
            }

            String auth = memberEntity.getMemberAuth(); // role : ROLE_USER, ROLE_ADMIN

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(auth));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(memberId, null, authorities);

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
