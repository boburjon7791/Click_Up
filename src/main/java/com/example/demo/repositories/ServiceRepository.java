package com.example.demo.repositories;

import com.example.demo.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long>, JpaSpecificationExecutor<Service> {
    Boolean existsByNameOrRestApiOrBankAccount(String name, String restApi, String bankAccount);

    @Transactional
    void updateEnableTrueById(Long id);

    @Transactional
    void updateEnableFalseById(Long id);

    Optional<Service> findByIdAndEnableTrue(Long serviceId);
}