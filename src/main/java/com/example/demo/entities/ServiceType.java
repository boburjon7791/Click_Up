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
@Table(name = "service_type", indexes = {
        @Index(name = "idx_servicetype_name", columnList = "name")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_servicetype_name", columnNames = {"name"})
})
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Builder.Default
    private Boolean enable=true;
}
