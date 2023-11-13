package com.example.demo.payloads.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SupportBankUpdateDto {
    @NotNull
    @PositiveOrZero
    public Integer id;

    @NotBlank
    public String name;

    @Builder.Default
    public Boolean enable=true;

    @NotBlank
    public String restApi;
}
