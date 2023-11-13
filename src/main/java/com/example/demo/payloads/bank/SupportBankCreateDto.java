package com.example.demo.payloads.bank;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SupportBankCreateDto {
    @NotBlank
    public String name;

    @NotBlank
    public String restApi;
}
