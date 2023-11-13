package com.example.demo.mappers;

import com.example.demo.entities.Transaction;
import com.example.demo.payloads.transaction.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface TransactionMapper {
    TransactionMapper TRANSACTION_MAPPER = Mappers.getMapper(TransactionMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fromCard", ignore = true)
    @Mapping(target = "fromUser", ignore = true)
    @Mapping(target = "toUser", ignore = true)
    @Mapping(target = "toCard", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "confirmCode", ignore = true)
    @Mapping(target = "expireTimeConfirmation", ignore = true)
    Transaction toEntity(TransactionDto dto);

    @Mapping(target = "fromCardNumber", ignore = true)
    @Mapping(target = "toCardNumber",ignore = true)
    @Mapping(target = "fromUserId", ignore = true)
    @Mapping(target = "toUserId", ignore = true)
    @Mapping(target = "serviceId", ignore = true)
    TransactionDto toDto(Transaction transaction);

    default Page<TransactionDto> toDto(Page<Transaction> transactions){
        if (transactions==null || transactions.isEmpty()) {
            return Page.empty();
        }
        List<Transaction> content = transactions.getContent();
        Pageable pageable = transactions.getPageable();
        List<TransactionDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}
