package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "initialized")
public class Initialized {
    @Id
    @ManyToOne
    private AuthUser authUser;

    @Column(nullable = false)
    private String data;
}
