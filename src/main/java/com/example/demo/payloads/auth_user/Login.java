package com.example.demo.payloads.auth_user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class Login {
    @NotBlank
    public String email;

    @NotBlank
    public String password;
}
