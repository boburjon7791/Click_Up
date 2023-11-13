package com.example.demo.configuration;

import com.example.demo.payloads.auth_user.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import java.io.OutputStream;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(
                            "/swagger-ui.html",
                            "/swagger-ui/*",
                            "/api/*",
                            "/v3/*","/v1/*","/v2/*").permitAll();
                    registry.anyRequest().authenticated();
                })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(entryPoint()))
                .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler()))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationEntryPoint entryPoint(){
        return (request, response, authException) -> {
            String requestURI = request.getRequestURI();
            OutputStream outputStream = response.getOutputStream();
            String message = authException.getMessage();
            ErrorDto errorDto = ErrorDto.builder()
                    .code(401)
                    .message(message)
                    .uri(requestURI)
                    .build();
            objectMapper.writeValue(outputStream, errorDto);
        };
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, accessDeniedException) -> {
            String requestURI = request.getRequestURI();
            OutputStream outputStream = response.getOutputStream();
            String message = accessDeniedException.getMessage();
            ErrorDto errorDto = ErrorDto.builder()
                    .uri(requestURI)
                    .code(403)
                    .message(message)
                    .build();
            objectMapper.writeValue(outputStream, errorDto);
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("/**"));
        configuration.setAllowedHeaders(List.of("*"));

        PathPatternParser parser = new PathPatternParser();
        PathPattern pathPattern = parser.parse("/**");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(pathPattern.getPatternString(), configuration);
        return source;
    }
}
