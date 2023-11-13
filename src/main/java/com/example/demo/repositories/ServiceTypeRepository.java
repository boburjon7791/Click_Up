package com.example.demo.repositories;

import com.example.demo.entities.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long>, JpaSpecificationExecutor<ServiceType> {
    Boolean existsByName(String name);

    @Transactional
    void updateEnableTrueById(Long id);

    @Transactional
    void updateEnableFalseById(Long id);
}