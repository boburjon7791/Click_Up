package com.example.demo.payloads.role;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleCreateDto {
    @NotBlank
    public String name;
}
