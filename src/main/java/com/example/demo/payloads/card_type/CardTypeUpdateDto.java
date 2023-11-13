package com.example.demo.payloads.card_type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardTypeUpdateDto {
    @NotNull
    @PositiveOrZero
    public Integer id;

    @NotBlank
    public String name;

    @NotNull
    @Builder.Default
    public Boolean enable=true;

    @NotBlank
    public String restApi;
}
