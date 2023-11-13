package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "card", indexes = {
        @Index(name = "idx_card_expire_date",
                columnList = "expire_date, support_card_type_id, support_bank_id, phone_number, balance")
})
public class Card {
    @Id
    @NotBlank
    @Column(updatable = false, nullable = false)
    private String number;

    @PositiveOrZero
    @NotNull
    @Column(nullable = false)
    @Builder.Default
    private Double balance=0d;

    @Column(nullable = false, name = "expire_date",updatable = false)
    private LocalDate expireDate;

    @ManyToOne
    @JoinColumn(name = "support_card_type_id",updatable = false)
    private SupportCardType supportCardType;

    @ManyToOne
    @JoinColumn(name = "support_bank_id",updatable = false)
    private SupportBank supportBank;

    @NotBlank
    @Column(nullable = false,updatable = false)
    private String owner;

    @NotBlank
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Builder.Default
    private Boolean active=true;

    @ManyToOne
    @JoinColumn(name = "auth_user_id",updatable = false)
    private AuthUser authUser;
}
