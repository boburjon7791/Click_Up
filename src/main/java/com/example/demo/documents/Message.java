package com.example.demo.documents;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Message {
    @Id
    private UUID id;

    @NotBlank
    private String fromPhoneNumber;

    @NotBlank
    private String toPhoneNumber;

    @NotNull
    @Positive
    private Double balance;

    @NotNull
    private Boolean success;
}
