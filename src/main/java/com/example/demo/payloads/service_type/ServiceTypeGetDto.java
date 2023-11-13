package com.example.demo.payloads.service_type;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServiceTypeGetDto {
    public Long id;
    public String name;
}
