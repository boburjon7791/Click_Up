package com.example.demo.payloads.service;

import com.example.demo.payloads.location.LocationGetDto;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServiceGetDto {
    public Long id;

    public String name;

    public Long serviceTypeId;

    @Builder.Default
    public Boolean enable=true;

    public Set<LocationGetDto> locations;
}
