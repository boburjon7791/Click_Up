package com.example.demo.service;

import com.example.demo.entities.AuthUser;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repositories.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username==null || username.isBlank()) {
            return null;
        }
        AuthUser authUser = authUserRepository.findByEmail(username).orElseThrow(NotFoundException::new);
        Set<SimpleGrantedAuthority> authoritySet = authUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return User.builder()
                .accountLocked(authUser.getActive())
                .username(authUser.getEmail())
                .password(authUser.getPinCode().toString())
                .authorities(authoritySet)
                .accountExpired(authUser.getActive())
                .credentialsExpired(authUser.getActive())
                .build();
    }
}
