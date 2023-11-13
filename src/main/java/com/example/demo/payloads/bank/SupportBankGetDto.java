package com.example.demo.payloads.bank;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SupportBankGetDto {
    public Integer id;

    public String name;

    public Boolean enable;
}
