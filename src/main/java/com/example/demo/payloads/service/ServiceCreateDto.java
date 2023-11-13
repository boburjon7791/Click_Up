package com.example.demo.payloads.service;

import com.example.demo.payloads.location.LocationCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServiceCreateDto {
    @NotBlank
    public String name;

    @NotNull
    public Long serviceTypeId;

    @NotBlank
    public String restApi;

    @NotNull
    public String bankAccount;

    @NotNull
    public Set<LocationCreateDto> locations;
}
