package com.example.demo.payloads.card;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardCreateDto {
    @NotBlank
    public String number;

    @PositiveOrZero
    @NotNull
    public Double balance;

    @Future
    @NotNull
    public LocalDate expireDate;

    @NotBlank
    public String cardType;

    @NotBlank
    public String bank;

    @NotBlank
    public String owner;

    @NotBlank
    @Pattern(regexp = "[+][0-9]{12}")
    public String phoneNumber;

    @NotNull
    public Long userId;
}
