package com.example.demo.repositories;

import com.example.demo.entities.ConfirmedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConfirmedUserRepository extends JpaRepository<ConfirmedUser, String>, JpaSpecificationExecutor<ConfirmedUser> {
    Boolean existsByPersonalNumberOrIdCardNumber(String personalNumber, String idCardNumber);
    Boolean existsByPersonalNumber(String personalNumber);
    Boolean existsByIdCardNumber(String idCardNumber);

    @Query(value = "select exists (from ConfirmedUser cu where cu.authUser.id=?1)")
    Boolean existsByUserId(Long id);

    @Query(value = "from ConfirmedUser cu where cu.authUser.id=?1 and cu.authUser.active=true")
    Optional<ConfirmedUser> findByUserIdAndActiveTrue(Long id);
}