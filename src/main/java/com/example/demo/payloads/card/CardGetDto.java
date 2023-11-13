package com.example.demo.payloads.card;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardGetDto {
    public String number;

    public Double balance;

    public LocalDate expireDate;

    public String cardType;

    public String bank;

    public String owner;

    public String phoneNumber;

    public Long userId;

    public Boolean active;
}
