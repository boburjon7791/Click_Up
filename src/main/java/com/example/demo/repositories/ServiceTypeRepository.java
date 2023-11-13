package com.example.demo.repositories;

import com.example.demo.entities.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long>, JpaSpecificationExecutor<ServiceType> {
    Boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query(value = "update ServiceType t set t.enable=true where t.id=?1")
    void updateEnableTrueById(Long id);

    @Transactional
    @Modifying
    @Query(value = "update ServiceType t set t.enable=false where t.id=?1")
    void updateEnableFalseById(Long id);
}