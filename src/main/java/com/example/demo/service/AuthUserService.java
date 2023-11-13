package com.example.demo.service;

import com.example.demo.payloads.auth_user.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthUserService {
    void register(AuthUserCreateDto dto);
    void login1(LoginDto loginDto, HttpServletResponse response);
    AuthUserGetDto login2(String confirmCode, HttpServletRequest request, Long userId, String data, HttpServletResponse response);
    void logout(Long id, HttpServletRequest request, String data);
    AuthUserGetDto get(Long id);
    void enable(Long id);
    void disable(Long id);
    void editRole(String role, Long id);
    ConfirmedUserDto create(ConfirmedUserDto dto);
    ConfirmedUserDto getData(Long id);
    AuthUserGetDto initialize(Short pinCode, HttpServletRequest request);
    AuthUserGetDto update(AuthUserUpdateDto dto);
    Page<AuthUserGetDto> users(Pageable pageable);
}
