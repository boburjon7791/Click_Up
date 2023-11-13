package com.example.demo.payloads.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionDto {
    public UUID id;

    @Builder.Default
    public LocalDateTime date=LocalDateTime.now();

    @NotBlank
    public String fromCardNumber;

    @NotNull
    @PositiveOrZero
    public Double summa;

    @NotNull
    @PositiveOrZero
    public Long fromUserId;

    public String toCardNumber;
    public Long toUserId;

    public Long serviceId;

    public String bankAccount;

    @Builder.Default
    public Boolean success=true;

    @Builder.Default
    public Short commission =0;
}
