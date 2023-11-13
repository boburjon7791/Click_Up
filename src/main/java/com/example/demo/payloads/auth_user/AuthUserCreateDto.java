package com.example.demo.payloads.auth_user;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthUserCreateDto {
    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    public String fatherName;

    @NotNull
    @Past
    public LocalDate birthdate;

    @NotBlank
    @Pattern(regexp = "[+][0-9]{12}")
    public String phoneNumber;

    @NotBlank
    @Email
    public String email;

    @NotNull
    public Short pinCode;

    public String profileImage;

    @NotBlank
    public String password;
}
