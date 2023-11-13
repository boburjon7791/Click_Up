package com.example.demo.repositories;

import com.example.demo.entities.AuthUser;
import com.example.demo.entities.ConfirmCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, AuthUser>, JpaSpecificationExecutor<ConfirmCode> {
    Optional<ConfirmCode> findByConfirmPassword(Integer confirmCode);

    @Modifying
    @Transactional
    @Async
    @Query(nativeQuery = true, value = "delete from confirmation_code where auth_user_id=?1")
    void deleteById(Long userId);
}