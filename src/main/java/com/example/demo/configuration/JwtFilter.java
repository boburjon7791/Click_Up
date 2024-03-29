package com.example.demo.configuration;

import com.example.demo.service.CustomUserDetails;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.demo.utils.JwtTokenUtils.getEmail;
import static com.example.demo.utils.JwtTokenUtils.isValid;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String remoteHost = request.getRemoteHost();
        String userInfo = request.getHeader("User-Agent");
        log.info(userInfo+" : "+remoteHost);
        if (authorization==null || authorization.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }
        if(!isValid(authorization)){
            filterChain.doFilter(request, response);
            return;
        }
        String email = getEmail(authorization);
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails(request);
        log.info("details : "+details.getRemoteAddress());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
