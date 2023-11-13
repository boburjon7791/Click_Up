package com.example.demo.repositories;

import com.example.demo.entities.SupportCardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SupportCardTypeRepository extends JpaRepository<SupportCardType, Integer>, JpaSpecificationExecutor<SupportCardType> {
    Boolean existsByName(String cardType);

    @Transactional
    void updateEnableTrueById(Integer id);

    @Transactional
    void updateEnableFalseById(Integer id);

    Boolean existsByNameAndEnableTrue(String cardType);
}