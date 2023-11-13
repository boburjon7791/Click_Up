package com.example.demo.service;

import com.example.demo.payloads.card.CardCreateDto;
import com.example.demo.payloads.card.CardGetDto;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface CardService {
    CardGetDto create(CardCreateDto dto, HttpServletRequest request);
    CardGetDto get(String number);
    void enable(String number);
    void disable(String number);
    Page<CardGetDto> cards(Pageable pageable);
    List<CardGetDto> myCards(Long userId);
    void check(Map<String, String> details);
}
