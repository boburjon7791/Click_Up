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
public class CardTypeGetDto {
    public Integer id;

    public String name;

    public Boolean enable;
}
