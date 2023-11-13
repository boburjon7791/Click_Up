package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "initialized")
public class Initialized {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "auth_user_id",updatable = false)
    private AuthUser authUser;

    @Column(nullable = false)
    private String data;
}
