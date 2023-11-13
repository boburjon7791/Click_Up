package com.example.demo.payloads.card_type;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardTypeCreateDto {
    @NotBlank
    public String name;

    @NotBlank
    public String restApi;
}
