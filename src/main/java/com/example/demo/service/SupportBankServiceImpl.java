package com.example.demo.service;

import com.example.demo.entities.SupportBank;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.payloads.bank.SupportBankCreateDto;
import com.example.demo.payloads.bank.SupportBankGetDto;
import com.example.demo.payloads.bank.SupportBankUpdateDto;
import com.example.demo.repositories.SupportBankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.mappers.SupportBankMapper.SUPPORT_BANK_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportBankServiceImpl implements SupportBankService {
    private final SupportBankRepository supportBankRepository;
    @Override
    public SupportBankGetDto create(SupportBankCreateDto dto) {
        if (supportBankRepository.existsByName(dto.name)) {
            throw new BadRequestException("This bank name already exist");
        }
        SupportBank supportBank = SUPPORT_BANK_MAPPER.toEntity(dto);
        SupportBank saved = supportBankRepository.save(supportBank);
        log.info("{} saved",saved);
        return SUPPORT_BANK_MAPPER.toDto(saved);
    }

    @Override
    public SupportBankGetDto update(SupportBankUpdateDto dto) {
        if (supportBankRepository.existsByName(dto.name)) {
            throw new BadRequestException("This bank name already exist");
        }
        SupportBank supportBank = SUPPORT_BANK_MAPPER.toEntity(dto);
        SupportBank updated = supportBankRepository.save(supportBank);
        log.info("{} updated",updated);
        return SUPPORT_BANK_MAPPER.toDto(updated);
    }

    @Override
    public SupportBankGetDto get(Integer id) {
        SupportBank supportBank = supportBankRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("This bank not allowed"));
        return SUPPORT_BANK_MAPPER.toDto(supportBank);
    }

    @Override
    public void enable(Integer id) {
        if (!supportBankRepository.existsById(id)) {
            throw new BadRequestException("This bank not allowed");
        }
        supportBankRepository.updateEnableTrueById(id);
    }

    @Override
    public void disable(Integer id) {
        if (!supportBankRepository.existsById(id)) {
            throw new BadRequestException("This bank not allowed");
        }
        supportBankRepository.updateEnableFalseById(id);
    }

    @Override
    public Page<SupportBankGetDto> supportBanks(Pageable pageable) {
        Page<SupportBank> all = supportBankRepository.findAll(pageable);
        log.info("{} elements gave",all.getTotalElements());
        return SUPPORT_BANK_MAPPER.toDto(all);
    }
}
