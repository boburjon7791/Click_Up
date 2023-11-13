package com.example.demo.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "support_bank", uniqueConstraints = {
        @UniqueConstraint(name = "uc_supportbank_name",
                columnNames = {"name"})
})
public class SupportBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Builder.Default
    private Boolean enable=true;

    @NotBlank
    private String restApi;
}
