package com.example.demo.payloads.role;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleGetDto {
    public Integer id;

    public String name;
}
