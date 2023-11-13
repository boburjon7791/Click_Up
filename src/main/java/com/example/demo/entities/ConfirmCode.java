package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "confirm_code", indexes = {
        @Index(name = "idx_confirmcode", columnList = "confirm_password, expire_time")
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ConfirmCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "auth_user_id",nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private AuthUser authUser;

    @Column(nullable = false, name = "confirm_password")
    private Integer confirmPassword;

    @Column(nullable = false, name = "expire_time", updatable = false)
    @Builder.Default
    private LocalDateTime expireTime=LocalDateTime.now().plusMinutes(5);
}
