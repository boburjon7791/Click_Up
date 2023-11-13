package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.query.sqm.TemporalUnit;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "transaction", indexes = {
        @Index(name = "idx_transaction_date",
                columnList = "date, from_card_id, from_user_id, to_card_id, to_user_id, service_id")
})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime date=LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "from_card_id")
    private Card fromCard;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Double summa;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private AuthUser fromUser;

    @ManyToOne
    @JoinColumn(name = "to_card_id")
    private Card toCard;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private AuthUser toUser;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "bank_account")
    private String bankAccount;

    @Builder.Default
    private Boolean success=false;

    @Builder.Default
    private Short commission=0;

    @Builder.Default
    @Column(nullable = false, name = "confirm_code")
    private Integer confirmCode=new Random().nextInt(5,6);

    @Builder.Default
    @Column(nullable = false, name = "expire_time_confirmation")
    private LocalDateTime expireTimeConfirmation=LocalDateTime.now().plusMinutes(3);
}
