package com.example.demo.payloads.service_type;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServiceTypeCreateDto {
    @NotBlank
    public String name;
}
