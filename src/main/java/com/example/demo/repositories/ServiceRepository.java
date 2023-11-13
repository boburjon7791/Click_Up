package com.example.demo.repositories;

import com.example.demo.entities.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long>, JpaSpecificationExecutor<Service> {
    Boolean existsByNameOrRestApiOrBankAccount(String name, String restApi, String bankAccount);

    @Transactional
    @Modifying
    @Query(value = "update Service s set s.enable=true where s.id=?1")
    void updateEnableTrueById(Long id);

    @Transactional
    @Modifying
    @Query(value = "update Service s set s.enable=false where s.id=?1")
    void updateEnableFalseById(Long id);

    Optional<Service> findByIdAndEnableTrue(Long serviceId);
}