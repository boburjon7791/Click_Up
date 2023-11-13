package com.example.demo.mappers;

import com.example.demo.entities.Card;
import com.example.demo.payloads.card.CardCreateDto;
import com.example.demo.payloads.card.CardGetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface CardMapper {
    CardMapper CARD_MAPPER = Mappers.getMapper(CardMapper.class);

    @Mapping(target = "cardType", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "bank", ignore = true)
    CardGetDto toDto(Card card);

    @Mapping(target = "supportBank", ignore = true)
    @Mapping(target = "supportCardType", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "authUser", ignore = true)
    Card toEntity(CardCreateDto dto);

    default Page<CardGetDto> toDto(Page<Card> cards){
        if (cards==null || cards.isEmpty()) {
            return Page.empty();
        }
        List<Card> content = cards.getContent();
        Pageable pageable = cards.getPageable();
        List<CardGetDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list,pageable, list.size());
    }
}
