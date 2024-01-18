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
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username==null || username.isBlank()) {
            throw new NotFoundException();
        }
        AuthUser authUser = authUserRepository.findByEmail(username).orElseThrow(NotFoundException::new);
        return new CustomUserDetails(authUser);
    }
}
