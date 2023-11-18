package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "auth_user",
        indexes = {
        @Index(name = "idx_auth_user_birthdate", columnList = "birthdate")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_authuser_id_card_number", columnNames = {"phone_number", "email", "main_card_id"})
})
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false,name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "father_name")
    private String fatherName;

    @NotNull
    @Column(nullable = false)
    private LocalDate birthdate;

    @Builder.Default
    private Boolean confirmed=false;

    @Builder.Default
    private Boolean active=false;

    @NotBlank
    @Pattern(regexp = "[+][0-9]{12}")
    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    @NotBlank
    @Column(nullable = false)
    @Email
    private String email;

    @NotNull
    @Column(nullable = false,name = "pin_code")
    private String pinCode;

    @ManyToMany(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "auth_user_role",
            joinColumns = @JoinColumn(name = "auth_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @NonNull
    private Set<Role> roles;

    @Column(name = "profile_image")
    private String profileImage;

    @OneToOne
    @JoinColumn(name = "main_card_id")
    private Card mainCard;

    @OneToMany(mappedBy = "authUser",
    cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Initialized> initialized;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Card> cards;

    @OneToOne(mappedBy = "authUser")
    private ConfirmCode code;

    @NotBlank
    private String password;
}
