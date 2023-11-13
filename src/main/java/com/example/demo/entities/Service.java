package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "service", indexes = {
        @Index(name = "idx_service_name_location_id", columnList = "name")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_service_name_restapi", columnNames = {"name", "rest_api", "bank_account"})
})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @NotBlank
    @Column(name = "rest_api",nullable = false)
    private String restApi;

    @Column(nullable = false,name = "bank_account")
    private String bankAccount;

    @Builder.Default
    private Boolean enable=true;

    @ManyToMany(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "service_location",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations;
}
