package com.example.demo.controller;

import com.example.demo.payloads.transaction.TransactionDto;
import com.example.demo.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api.support-card-type")
public class TransactionController {
    private final TransactionService transactionService;
    @Async
    @GetMapping("/get/full-name/{numberBase64}")
    public CompletableFuture<ResponseEntity<String>> getFullName(@PathVariable String numberBase64){
        String number = CardController.decode(numberBase64);
        String fullName = transactionService.getFullName(number);
        return CompletableFuture.completedFuture(ResponseEntity.ok(fullName));
    }

    @PostMapping("/create/1")
    public ResponseEntity<UUID> create(@RequestBody TransactionDto dto){
        UUID transactionId = transactionService.create(dto);
        return new ResponseEntity<>(transactionId, HttpStatus.CREATED);
    }
    @PostMapping("/create/2")
    public ResponseEntity<Short> create2(@RequestParam String transactionId,
                                                  @RequestParam Integer confirmCode,
                                                  HttpServletRequest request){
        Short transactionStatus = transactionService.create2(UUID.fromString(transactionId), request, confirmCode);
        return new ResponseEntity<>(transactionStatus,HttpStatus.CREATED);
    }
    @GetMapping("/get-all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<TransactionDto>> transactions(@RequestParam Integer page){
        Page<TransactionDto> transactions = transactionService.transactions(PageRequest.of(page, 10));
        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/get/my-transactions/{userId}")
    public ResponseEntity<Page<TransactionDto>> myTransactions(@PathVariable Long userId,
                                                               @RequestParam Integer page){
        Page<TransactionDto> myTransactions = transactionService.myTransactions(userId, PageRequest.of(page, 10));
        return ResponseEntity.ok(myTransactions);
    }
}
