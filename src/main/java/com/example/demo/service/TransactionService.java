package com.example.demo.service;

import com.example.demo.payloads.transaction.TransactionDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TransactionService {
    String getFullName(String number);
    UUID create(TransactionDto dto);
    Short create2(UUID transactionId, HttpServletRequest request, Integer confirmCode);
    Page<TransactionDto> transactions(Pageable pageable);
    Page<TransactionDto> myTransactions(Long id, Pageable pageable);
}
