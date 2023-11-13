package com.example.demo.payloads.location;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LocationGetDto {
    public Long id;

    public Double latitude;

    public Double longitude;
}
