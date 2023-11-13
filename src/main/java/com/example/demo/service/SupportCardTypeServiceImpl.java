package com.example.demo.service;

import com.example.demo.entities.SupportCardType;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.card_type.CardTypeCreateDto;
import com.example.demo.payloads.card_type.CardTypeGetDto;
import com.example.demo.payloads.card_type.CardTypeUpdateDto;
import com.example.demo.repositories.SupportCardTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.mappers.SupportCardTypeMapper.SUPPORT_CARD_TYPE_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportCardTypeServiceImpl implements SupportCardTypeService {
    private final SupportCardTypeRepository supportCardTypeRepository;
    @Override
    public CardTypeGetDto create(CardTypeCreateDto dto) {
        if (supportCardTypeRepository.existsByName(dto.name)) {
            throw new BadRequestException("This card type already exist");
        }
        SupportCardType cardType = SUPPORT_CARD_TYPE_MAPPER.toEntity(dto);
        SupportCardType saved = supportCardTypeRepository.save(cardType);
        log.info("{} saved",saved);
        return SUPPORT_CARD_TYPE_MAPPER.toDto(saved);
    }

    @Override
    public CardTypeGetDto update(CardTypeUpdateDto dto) {
        if (supportCardTypeRepository.existsByName(dto.name)) {
            throw new BadRequestException("This card type already exist");
        }
        SupportCardType cardType = SUPPORT_CARD_TYPE_MAPPER.toEntity(dto);
        SupportCardType updated = supportCardTypeRepository.save(cardType);
        log.info("{} updated",updated);
        return SUPPORT_CARD_TYPE_MAPPER.toDto(updated);
    }

    @Override
    public CardTypeGetDto get(Integer id) {
        SupportCardType cardType = supportCardTypeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("This card type already exist"));
        log.info("{} gave",cardType);
        return SUPPORT_CARD_TYPE_MAPPER.toDto(cardType);
    }

    @Override
    public void enable(Integer id) {
        if (!supportCardTypeRepository.existsById(id)) {
            throw new NotFoundException("Not allowed card type");
        }
        supportCardTypeRepository.updateEnableTrueById(id);
    }

    @Override
    public void disable(Integer id) {
        if(!supportCardTypeRepository.existsById(id)){
            throw new NotFoundException("Not allowed card type");
        }
        supportCardTypeRepository.updateEnableFalseById(id);
    }

    @Override
    public Page<CardTypeGetDto> cardTypes(Pageable pageable) {
        Page<SupportCardType> all = supportCardTypeRepository.findAll(pageable);
        log.info("{} elements gave",all.getTotalElements());
        return SUPPORT_CARD_TYPE_MAPPER.toDto(all);
    }
}
