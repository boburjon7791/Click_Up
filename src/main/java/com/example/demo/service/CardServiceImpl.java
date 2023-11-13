package com.example.demo.service;

import com.example.demo.entities.Card;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.card.CardCreateDto;
import com.example.demo.payloads.card.CardGetDto;
import com.example.demo.repositories.CardRepository;
import com.example.demo.repositories.SupportBankRepository;
import com.example.demo.repositories.SupportCardTypeRepository;
import com.example.demo.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.demo.mappers.CardMapper.CARD_MAPPER;
import static com.example.demo.utils.JwtTokenUtils.getEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final SupportBankRepository supportBankRepository;
    private final SupportCardTypeRepository supportCardTypeRepository;
    private final CardRepository cardRepository;

    @Override
    public CardGetDto create(CardCreateDto dto, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        String email = getEmail(authorization);
        if (cardRepository.existsByNumberAndUserEquals(dto.number, email)) {
            Runnable runnable = ()-> cardRepository.updateActiveTrueByNumber(dto.number);
            runnable.run();
            Card card = cardRepository.findByNumber(dto.number)
                    .orElseThrow(() -> new NotFoundException("Card not found"));
            return CARD_MAPPER.toDto(card);
        }
        check(Map.of(
                "card", dto.cardType,
                "bank", dto.bank,
                "number", dto.number
        ));
        Card card = CARD_MAPPER.toEntity(dto);
        Card saved = cardRepository.save(card);
        log.info("{} saved",saved);
        return CARD_MAPPER.toDto(saved);
    }

    @Override
    public CardGetDto get(String number) {
        Card card = cardRepository.findByNumber(number)
                .orElseThrow(() -> new NotFoundException("Card is not exist"));
        check(Map.of(
                "card",card.getSupportCardType().getName(),
                "bank",card.getSupportBank().getName()
        ));
        log.info("{} gave",card);
        return CARD_MAPPER.toDto(card);
    }

    @Override
    public void enable(String number) {
        if (!cardRepository.existsByNumber(number)) {
            throw new NotFoundException("Card is not exist");
        }
        cardRepository.updateActiveTrueByNumber(number);
    }

    @Override
    public void disable(String number) {
        if (!cardRepository.existsByNumber(number)) {
            throw new NotFoundException("Card is not exist");
        }
        cardRepository.updateActiveFalseByNumber(number);
    }

    @Override
    public Page<CardGetDto> cards(Pageable pageable) {
        Page<Card> all = cardRepository.findAll(pageable);
        log.info("{} elements gave",all.getTotalElements());
        return CARD_MAPPER.toDto(all);
    }

    @Override
    public List<CardGetDto> myCards(Long userId) {
        List<Card> myCards=cardRepository.findAllByUserId(userId);
        log.info("{} cards gave to {}",myCards.size(),userId);
        return myCards.stream()
                .map(CARD_MAPPER::toDto)
                .toList();
    }

    @Override
    public void check(Map<String, String> details) {
        String number = details.get("number");
        String bank = details.get("bank");
        String card = details.get("card");
        if (card!=null && cardRepository.existsByNumber(number)) {
            throw new BadRequestException("Already exists this card");
        }
        if(!supportCardTypeRepository.existsByName(bank)){
            throw new NotFoundException("Not allowed card type");
        }
        if(supportBankRepository.existsByName(bank)){
            throw new NotFoundException("Not allowed bank");
        }
    }

}
