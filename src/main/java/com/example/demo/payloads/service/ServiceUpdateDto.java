package com.example.demo.payloads.service;

import com.example.demo.payloads.location.LocationGetDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServiceUpdateDto {
    @NotNull
    @PositiveOrZero
    public Long id;

    @NotBlank
    public String name;

    @NotNull
    public Long serviceTypeId;

    @NotBlank
    public String restApi;

    @NotNull
    public String bankAccount;

    @NotNull
    public Set<LocationGetDto> locations;
}
