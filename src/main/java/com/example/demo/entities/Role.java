package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(name = "uc_role_name",
                columnNames = {"name"})
})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private Boolean active;

    @ManyToMany(mappedBy = "roles")
    private List<AuthUser> authUsers;
}
