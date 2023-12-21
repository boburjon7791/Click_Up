package com.example.demo.repositories;

import com.example.demo.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    @Query(value = "from Transaction t where t.fromUser.id=?1")
    Page<Transaction> findAllByUserId(Long id, Pageable pageable);


    @Query(value = """
    update Transaction t set t.fromCard.balance=t.fromCard.balance-(t.summa+t.commission),
    t.toCard.balance=t.toCard.balance+t.summa
    where t.id=?1 and t.confirmCode=?2 and t.fromUser.active=true and t.fromCard.active=true
    and t.toCard.active and t.fromCard.supportBank.enable=true and t.fromCard.supportCardType.enable=true
    and t.fromCard.expireDate<=now() and t.toCard.supportBank.enable=true and t.toCard.supportCardType.enable=true
    and t.toCard.expireDate<=now() and t.fromCard.balance>=(t.summa+t.commission) and t.fromCard.balance>=(t.summa+t.commission)
    """)
    @Modifying
    @Transactional
    void transactionCreate2WithToCard(UUID id, Integer code);

    @Query(value = """
    update Transaction t set t.fromCard.balance=t.fromCard.balance-(t.summa+t.commission),
    t.toUser.mainCard.balance=t.toCard.balance+t.summa
    where t.id=?1 and t.confirmCode=?2 and t.fromUser.active=true and t.fromCard.active=true
    and t.toUser.active=true and t.toUser.mainCard.active=true and t.fromCard.supportCardType.enable=true
    and t.fromCard.supportBank.enable=true and t.fromCard.expireDate<=now() and t.fromUser.mainCard.supportBank.enable=true
    and t.fromUser.mainCard.supportCardType.enable=true and t.fromUser.mainCard.expireDate<=now()
    """)
    @Modifying
    @Transactional
    void transactionCreate2WithToUser(UUID id, Integer code);

    @Query(value = """
    update Transaction t set t.fromCard.balance=t.fromCard.balance-(t.summa+t.commission)
    where t.id=?1 and t.confirmCode=?2 and t.fromUser.active=true and t.fromCard.active=true
    and t.service.enable=true and t.service.bankAccount<>null and length(t.service.bankAccount)>10
    and t.fromCard.supportCardType.enable=true and t.fromCard.supportBank.enable=true
    and t.fromCard.expireDate<=now() and t.fromCard.balance>=(t.summa+t.commission)
    """)
    @Modifying
    @Transactional
    void transactionCreate2WithToService(UUID id, Integer code);

    @Query(value = """
    update Transaction t set t.fromCard.balance=t.fromCard.balance-(t.summa+t.commission)
    where t.id=?1 and t.confirmCode=?2 and t.fromUser.active=true and t.fromCard.active=true
    and t.bankAccount<>null and length(t.bankAccount)>10 and t.fromCard.supportCardType.enable=true
    and t.fromCard.supportBank.enable=true and t.fromCard.expireDate<=now()
    and t.fromCard.balance>=(t.summa+t.commission)
    """)
    @Modifying
    @Transactional
    void transactionCreate2WithToBankAccount(UUID id, Integer code);
}