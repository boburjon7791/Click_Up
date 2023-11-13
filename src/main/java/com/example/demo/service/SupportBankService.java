package com.example.demo.service;

import com.example.demo.payloads.bank.SupportBankCreateDto;
import com.example.demo.payloads.bank.SupportBankGetDto;
import com.example.demo.payloads.bank.SupportBankUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface SupportBankService {
    SupportBankGetDto create(SupportBankCreateDto dto);
    SupportBankGetDto update(SupportBankUpdateDto dto);
    SupportBankGetDto get(Integer id);
    void enable(Integer id);
    void disable(Integer id);
    Page<SupportBankGetDto> supportBanks(Pageable pageable);
}
