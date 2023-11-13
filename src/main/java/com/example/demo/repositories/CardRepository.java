package com.example.demo.repositories;

import com.example.demo.entities.Card;
import com.github.javafaker.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    @Query(value = "from Card c where c.number=?1 and c.active=false and c.authUser.email=?2")
    Boolean existsByNumberAndUserEquals(String cardType, String email);

    Optional<Card> findByNumber(String number);

    @Transactional
    @Modifying
    @Query(value = "update Card c set c.active=true where c.number=?1")
    void updateActiveTrueByNumber(String number);

    @Transactional
    @Modifying
    @Query(value = "update Card c set c.active=false where c.number=?1")
    void updateActiveFalseByNumber(String number);

    @Query(value = "from Card c where c.authUser.id=?1")
    List<Card> findAllByUserId(Long userId);

    Boolean existsByNumberAndActiveTrue(String fromCardNumber);

    Optional<Card> findByNumberAndActiveTrue(String fromCardNumber);

    Boolean existsByNumber(String number);
}