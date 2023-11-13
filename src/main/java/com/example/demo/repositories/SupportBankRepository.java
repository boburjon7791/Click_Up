package com.example.demo.repositories;

import com.example.demo.entities.SupportBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SupportBankRepository extends JpaRepository<SupportBank, Integer>, JpaSpecificationExecutor<SupportBank> {
    Boolean existsByName(String bank);

    @Transactional
    @Modifying
    @Query(value = "update SupportBank sb set sb.enable=true where sb.id=?1")
    void updateEnableTrueById(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update SupportBank sb set sb.enable=false where sb.id=?1")
    void updateEnableFalseById(Integer id);

    Boolean existsByNameAndEnableTrue(String bank);
}