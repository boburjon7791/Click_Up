package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "confirmed_user", indexes = {
        @Index(name = "idx_confirmeduser_address", columnList = "address, given_date, expire_date")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_confirmeduser", columnNames = {"auth_user_id", "id_card_number"})
})
public class ConfirmedUser {
    @Id
    @NotBlank
    @Column(name = "personal_number", nullable = false, updatable = false)
    public String personalNumber;

    @OneToOne(optional = false)
    @JoinColumn(updatable = false, name = "auth_user_id", nullable = false)
    private AuthUser authUser;

    @NotBlank
    @Column(nullable = false)
    public String address;

    @NotBlank
    @Column(name = "id_card_number", updatable = false, nullable = false)
    public String idCardNumber;

    @NotNull
    @Past
    @Column(name = "given_date", nullable = false)
    public LocalDate givenDate;

    @NotNull
    @Future
    @Column(name = "expire_date", nullable = false)
    public LocalDate expireDate;
}
