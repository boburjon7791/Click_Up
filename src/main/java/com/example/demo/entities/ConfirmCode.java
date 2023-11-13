package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ConfirmCode", indexes = {
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
    @OneToOne(fetch = FetchType.EAGER)
    private AuthUser authUser;

    @Column(nullable = false, name = "confirm_password")
    private Integer confirmPassword;

    @Column(nullable = false, name = "expire_time", updatable = false)
    @Builder.Default
    private LocalDateTime expireTime=LocalDateTime.now().plusMinutes(5);
}
