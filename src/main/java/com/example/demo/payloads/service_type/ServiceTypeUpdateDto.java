package com.example.demo.payloads.service_type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServiceTypeUpdateDto {
    @NotNull
    @PositiveOrZero
    public Long id;

    @NotBlank
    public String name;

    @NotNull
    @Builder.Default
    public Boolean enable=true;
}
