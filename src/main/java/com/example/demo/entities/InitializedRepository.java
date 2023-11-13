package com.example.demo.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InitializedRepository extends JpaRepository<Initialized, AuthUser>, JpaSpecificationExecutor<Initialized> {
    @Query(value = "from Initialized i where i.authUser.email=?1 and i.authUser.active=true")
    Optional<Initialized> findByUserEmailAndActiveTrue(String email);
}