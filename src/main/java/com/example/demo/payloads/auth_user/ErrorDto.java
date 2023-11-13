package com.example.demo.payloads.auth_user;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class ErrorDto {
    public int code;
    public String message;
    public String uri;
}
