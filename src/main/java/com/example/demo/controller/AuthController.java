package com.example.demo.controller;

import com.example.demo.payloads.auth_user.AuthUserCreateDto;
import com.example.demo.payloads.auth_user.AuthUserGetDto;
import com.example.demo.payloads.auth_user.ConfirmedUserDto;
import com.example.demo.payloads.auth_user.LoginDto;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.service.AuthUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
@RequestMapping("/api.auth")
public class AuthController {
    private final AuthUserService authUserService;
    private final AuthUserRepository authUserRepository;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthUserCreateDto dto, HttpServletResponse response){
        authUserService.register(dto,response);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/activate/account/{email}")
    public ResponseEntity<Void> activateAccount(HttpServletRequest request,
                                                @PathVariable String email){
        authUserService.activateNewAccount(request, email);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/login-1")
    public ResponseEntity<Void> login1(@RequestBody LoginDto loginDto,
                                       HttpServletResponse response){
        authUserService.login1(loginDto, response);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/login-2")
    public ResponseEntity<AuthUserGetDto> login2(@RequestParam Map<String, String> params,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response){
        AuthUserGetDto getDto = authUserService.login2(CardController.decode(params.get("confirmCode"))
                , request, Long.parseLong(params.get("userId")), params.get("data"), response);
        return ResponseEntity.ok(getDto);
    }
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Map<String, String> params,
                                       HttpServletRequest request
                                       ){
        authUserService.logout(Long.parseLong(params.get("userId")),request,params.get("data"));
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<AuthUserGetDto> get(@PathVariable Long id){
        AuthUserGetDto getDto = authUserService.get(id);
        return ResponseEntity.ok(getDto);
    }
    @PostMapping("/create/confirmed-user")
    public ResponseEntity<ConfirmedUserDto> create(@RequestBody ConfirmedUserDto dto){
        ConfirmedUserDto userDto = authUserService.create(dto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
    @GetMapping("/get/confirm-user/{id}")
    public ResponseEntity<ConfirmedUserDto> getConfirmUser(@PathVariable Long id){
        ConfirmedUserDto userDto = authUserService.getData(id);
        return ResponseEntity.ok(userDto);
    }
    @GetMapping("/initialize/{pinCode}")
    public ResponseEntity<AuthUserGetDto> initialize(HttpServletRequest request,
                                                     @PathVariable Short pinCode){
        AuthUserGetDto getDto = authUserService.initialize(pinCode, request);
        return ResponseEntity.ok(getDto);
    }
    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<AuthUserGetDto>> users(@RequestParam Integer page){
        Page<AuthUserGetDto> users = authUserService.users(PageRequest.of(page, 10));
        return ResponseEntity.ok(users);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/update-role")
    public ResponseEntity<Void> updateRole(@RequestParam String role,
                                           @RequestParam String id){
        authUserService.editRole(role, Long.parseLong(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email){
        return ResponseEntity.ok(authUserRepository.existsByEmail(email));
    }
    @GetMapping("/get/exists/phone/{phone}")
    public ResponseEntity<Boolean> existsByPhone(@PathVariable String phone){
        return ResponseEntity.ok(authUserRepository.existsByPhoneNumber(phone));
    }
}
