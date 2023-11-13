package com.example.demo.payloads.auth_user;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class ConfirmedUserDto {
    @NotNull
    @Positive
    public Long id;

    @NotBlank
    public String address;

    @NotBlank
    public String idCardNumber;

    @Past
    @NotNull
    public LocalDate givenDate;

    @Future
    @NotNull
    public LocalDate expireDate;

    @NotBlank
    public String personalNumber;
}
