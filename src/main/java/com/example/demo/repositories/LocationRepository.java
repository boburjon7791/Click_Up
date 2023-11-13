package com.example.demo.repositories;

import com.example.demo.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {
    @Transactional
    @Modifying
    @Query(value = "update Location l set l.active=true where l.id=?1")
    void updateActiveTrueById(Long id);

    @Transactional
    @Modifying
    @Query(value = "update Location l set l.active=false where l.id=?1")
    void updateActiveFalseById(Long id);
}