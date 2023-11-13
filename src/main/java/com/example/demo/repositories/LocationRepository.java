package com.example.demo.repositories;

import com.example.demo.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {
    @Transactional
    void updateActiveTrueById(Long id);

    @Transactional
    void updateActiveFalseById(Long id);
}