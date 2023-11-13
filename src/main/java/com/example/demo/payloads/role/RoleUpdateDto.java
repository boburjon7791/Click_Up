package com.example.demo.payloads.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleUpdateDto {
    @NotNull
    @PositiveOrZero
    public Integer id;

    @NotBlank
    public String name;
}
