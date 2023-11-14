package com.example.demo.repositories;

import com.example.demo.entities.AuthUser;
import com.example.demo.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long>, JpaSpecificationExecutor<AuthUser> {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByEmailAndActiveTrue(String email);
    Optional<AuthUser> findByPhoneNumberAndActiveTrue(String email);
    Optional<AuthUser> findByEmailAndPhoneNumberAndActiveTrue(@NotBlank @Email String email, @NotBlank @Pattern(regexp = "[+][0-9]{12}") String phoneNumber);

    Boolean existsByIdAndActiveTrue(Long fromUserId);

    Optional<AuthUser> findByIdAndActiveTrue(Long fromUserId);

    Boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    Boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update AuthUser u set u.active=true where u.id=?1")
    void updateActiveTrueById(Long id);

    @Transactional
    @Modifying
    @Query(value = "update AuthUser u set u.active=false  where u.id=?1")
    void updateActiveFalseById(Long id);

    Boolean existsByPhoneNumber(String phone);

    @Query(value = "select exists (from AuthUser u where u.email=?1 and u.active=true and exists (from u.cards c where c.number=?2))")
    Boolean existsThisCardInUserAndUserActiveTrue(String email, String cardNumber);

    @Query(value = "update AuthUser u set u.active=true where u.email=?1")
    @Modifying
    @Transactional
    void updateActiveTrueByEmail(String email);
}