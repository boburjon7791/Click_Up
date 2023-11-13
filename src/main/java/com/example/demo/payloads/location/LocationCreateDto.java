package com.example.demo.payloads.location;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LocationCreateDto {
    @NotNull
    @PositiveOrZero
    public Double latitude;

    @NotNull
    @PositiveOrZero
    public Double longitude;
}
