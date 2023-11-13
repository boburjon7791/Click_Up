package com.example.demo.service;

import com.example.demo.payloads.card_type.CardTypeCreateDto;
import com.example.demo.payloads.card_type.CardTypeGetDto;
import com.example.demo.payloads.card_type.CardTypeUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SupportCardTypeService {
    CardTypeGetDto create(CardTypeCreateDto dto);
    CardTypeGetDto update(CardTypeUpdateDto dto);
    CardTypeGetDto get(Integer id);
    void enable(Integer id);
    void disable(Integer id);
    Page<CardTypeGetDto> cardTypes(Pageable pageable);
}
