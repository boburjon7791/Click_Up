package com.example.demo.service;

import com.example.demo.entities.*;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ForbiddenAccessException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.auth_user.*;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.repositories.ConfirmCodeRepository;
import com.example.demo.repositories.ConfirmedUserRepository;
import com.example.demo.repositories.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.mappers.AuthUserMapper.AUTH_USER_MAPPER;
import static com.example.demo.mappers.ConfirmedUserMapper.CONFIRMED_USER_MAPPER;
import static com.example.demo.utils.JwtTokenUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
    private final ConfirmedUserRepository confirmedUserRepository;
    private final ConfirmCodeRepository confirmCodeRepository;
    private final AuthUserRepository authUserRepository;
    private final JavaMailSenderService javaMailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MultimediaService multimediaService;

    @Override
    public void register(AuthUserCreateDto dto, HttpServletResponse response) {
        if(authUserRepository.existsByEmailOrPhoneNumber(dto.email, dto.phoneNumber)){
            throw new BadRequestException("Email or phone number already exist");
        }
        multimediaService.existsImage(dto.profileImage);
        AuthUser authUser = AUTH_USER_MAPPER.toEntity(dto);
        String encodedPassword = passwordEncoder.encode(dto.password);
        String encodedPinCode = passwordEncoder.encode(dto.pinCode.toString());
        authUser.setPassword(encodedPassword);
        authUser.setPinCode(encodedPinCode);
        AuthUser saved = authUserRepository.save(authUser);
        log.info("{} saved",saved);
        response.setHeader("email",encode(saved.getEmail()));
    }

    @Override
    public void activateNewAccount(HttpServletRequest request, String email) {
        String encodedEmail = request.getHeader("email");
        String decoded = decode(encodedEmail);
        if (decoded.equals(email) && authUserRepository.existsByEmail(email)) {
            authUserRepository.updateActiveTrueByEmail(email);
            return;
        }
        throw new BadRequestException("Email incorrect");
    }

    @Override
    public void login1(Login login, HttpServletResponse response) {
        AuthUser authUser = authUserRepository.findByEmailAndActiveTrue(login.email)
                .orElseThrow(() -> new NotFoundException("This email was not signed up"));
        if (!passwordEncoder.matches(login.password, authUser.getPassword())) {
            throw new BadRequestException("Incorrect password");
        }
        ConfirmCode confirmCode = ConfirmCode.builder()
                .authUser(authUser)
                .confirmPassword(new Random().nextInt(10000, 99999))
                .build();
        Integer confirmPassword = confirmCode.getConfirmPassword();
        String message = """
                <h3>This is confirmation code for login.</h3>
                <h3>Don't give anyone this confirmation code !!!</h3>
                <h1>%s</h1>
                """.formatted(confirmPassword);
        System.out.println(message);
        javaMailSenderService.send(authUser.getEmail(), message);
        String encoded = encode(authUser.getEmail());
        response.setHeader("email", encoded);
        Runnable runnable = ()->{
            confirmCodeRepository.save(confirmCode);
        };
        runnable.run();
    }

    @Override
    public AuthUserGetDto login2(String confirmCode, HttpServletRequest request,
                                 String data, HttpServletResponse response) {
        String encoded = request.getHeader("email");
        String email = decode(encoded);
        System.out.println("confirmCode = " + confirmCode);
        ConfirmCode confirmation = confirmCodeRepository.findByConfirmPassword(Integer.parseInt(confirmCode))
                .orElseThrow(()->new BadRequestException("Confirmation code is not correct"));
        AuthUser authUser = confirmation.getAuthUser();
        if (!authUser.getEmail().equals(email)) {
            throw new BadRequestException("User not found");
        }
        Set<Initialized> initializedSet = authUser.getInitialized();
        if (initializedSet.size()>3) {
            throw new ForbiddenAccessException("This account signed in with 3 devices");
        }
        Initialized initialized = Initialized.builder()
                .authUser(authUser)
                .data(data)
                .build();
        initializedSet.add(initialized);
        authUser.setInitialized(initializedSet);
        authUserRepository.save(authUser);
        authUser.setActive(true);
        authUserRepository.save(authUser);
        confirmCodeRepository.deleteById(authUser.getId());
        response.setHeader("Authorization",generateToken(authUser.getEmail()));
        return AUTH_USER_MAPPER.toDto(authUser);
    }

    @Override
    public void logout(Long id, HttpServletRequest request, String data) {
        AuthUser authUser = authUserRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Optional<Initialized> first = authUser.getInitialized()
                .stream()
                .filter(initialized -> initialized.getData().equals(data))
                .findFirst();
        if (first.isPresent()) {
            Set<Initialized> initializedSet = authUser.getInitialized();
            initializedSet.removeIf(initialized -> initialized.getData().equals(data));
            authUser.setInitialized(initializedSet);
            authUserRepository.save(authUser);
        }
    }

    @Override
    public AuthUserGetDto get(Long id) {
        AuthUser authUser = authUserRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        log.info("{} gave ",authUser);
        return AUTH_USER_MAPPER.toDto(authUser);
    }

    @Override
    public void enable(Long id) {
        if (!authUserRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        authUserRepository.updateActiveTrueById(id);
    }

    @Override
    public void disable(Long id) {
        if (!authUserRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        authUserRepository.updateActiveFalseById(id);
    }

    @Override
    public void editRole(String role, Long id) {
        Role newRole = roleRepository.findByNameAndActiveTrue(role)
                .orElseThrow(() -> new NotFoundException("Role not found"));
        AuthUser authUser = authUserRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Set<Role> roles = authUser.getRoles();
        roles.add(newRole);
        authUser.setRoles(roles);
        authUserRepository.save(authUser);
    }

    @Override
    public ConfirmedUserDto create(ConfirmedUserDto dto) {
        AuthUser authUser = authUserRepository.findByIdAndActiveTrue(dto.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if(!confirmedUserRepository.existsByPersonalNumberOrIdCardNumber(dto.personalNumber, dto.idCardNumber)){
            throw new BadRequestException("This data already exist");
        }
        ConfirmedUser confirmedUser = ConfirmedUser.builder()
                .authUser(authUser)
                .expireDate(dto.expireDate)
                .address(dto.address)
                .idCardNumber(dto.idCardNumber)
                .givenDate(dto.givenDate)
                .personalNumber(dto.personalNumber)
                .build();
        // TODO: 11/11/2023 we need to check this user's all personal data from passport system
        ConfirmedUser confirmed = confirmedUserRepository.save(confirmedUser);
        log.info("{} confirmed", confirmed);
        return CONFIRMED_USER_MAPPER.toDto(confirmed);
    }

    @Override
    public ConfirmedUserDto getData(Long id) {
        if (!authUserRepository.existsByIdAndActiveTrue(id) || !confirmedUserRepository.existsByUserId(id)){
            throw new NotFoundException("User not found");
        }
        ConfirmedUser confirmedUser = confirmedUserRepository.findByUserIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        log.info("{} gave",confirmedUser);
        return CONFIRMED_USER_MAPPER.toDto(confirmedUser);
    }

    @Override
    public AuthUserGetDto initialize(Short pinCode, HttpServletRequest request) {
        String data = request.getHeader("Data");
        String email = getEmail(request.getHeader("Authorization"));
        AuthUser authUser = authUserRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Set<String> collect = authUser.getInitialized()
                .stream()
                .map(Initialized::getData)
                .collect(Collectors.toSet());
        if (!collect.contains(data)) {
            throw new ForbiddenAccessException("You are not signed in!");
        }
        if (!passwordEncoder.matches(pinCode.toString(), authUser.getPinCode())) {
            throw new BadRequestException("Pin code is not correct");
        }
        log.info("{} initialized with pin code",authUser);
        return AUTH_USER_MAPPER.toDto(authUser);
    }

    @Override
    public AuthUserGetDto update(AuthUserUpdateDto dto) {
        if (!authUserRepository.existsByIdAndActiveTrue(dto.id)) {
            throw new NotFoundException("User not found");
        }
        AuthUser authUser = AUTH_USER_MAPPER.toEntity(dto);
        AuthUser updated = authUserRepository.save(authUser);
        log.info("{} updated",updated);
        return AUTH_USER_MAPPER.toDto(updated);
    }

    @Override
    public Page<AuthUserGetDto> users(Pageable pageable) {
        Page<AuthUser> all = authUserRepository.findAll(pageable);
        log.info("{} elements gave",all.getTotalElements());
        return AUTH_USER_MAPPER.toDto(all);
    }
}
