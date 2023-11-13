package com.example.demo.documents;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post {
    @Id
    private UUID id;
    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotNull
    @PositiveOrZero
    private Long viewsCount;

    @NotNull
    @PositiveOrZero
    private Long likeCount;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String url;

    @NotBlank
    private String urlTitle;

    @NotBlank
    private String imageName;
}
