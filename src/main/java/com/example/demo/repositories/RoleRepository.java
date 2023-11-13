package com.example.demo.repositories;

import com.example.demo.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    Boolean existsByName(String name);

    @Transactional
    void updateActiveTrueById(Integer id);

    @Transactional
    void updateActiveFalseById(Integer id);

    @Query(nativeQuery = true, value = "select r.* from role r inner join auth_user u on u.role_id=r.id")
    List<Role> findAllByUserId(Long id);

    Boolean existsByNameAndActiveTrue(String role);

    Optional<Role> findByNameAndActiveTrue(String role);
}