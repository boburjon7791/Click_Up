package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Double latitude;

    @Builder.Default
    private Boolean active=true;

    @NotNull
    @Column(nullable = false)
    private Double longitude;

    @ManyToMany(mappedBy = "locations")
    public Set<Service> services;
}
