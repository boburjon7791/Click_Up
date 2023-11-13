package com.example.demo.mappers;

import com.example.demo.entities.SupportCardType;
import com.example.demo.payloads.card_type.CardTypeCreateDto;
import com.example.demo.payloads.card_type.CardTypeGetDto;
import com.example.demo.payloads.card_type.CardTypeUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface SupportCardTypeMapper {
    SupportCardTypeMapper SUPPORT_CARD_TYPE_MAPPER = Mappers.getMapper(SupportCardTypeMapper.class);

    SupportCardType toEntity(CardTypeCreateDto dto);
    SupportCardType toEntity(CardTypeUpdateDto dto);

    @Mapping(target = "restApi", ignore = true)
    CardTypeGetDto toDto(SupportCardType supportCardType);
    default Page<CardTypeGetDto> toDto(Page<SupportCardType> cardTypes){
        if (cardTypes==null || cardTypes.isEmpty()) {
            return Page.empty();
        }
        List<SupportCardType> content = cardTypes.getContent();
        Pageable pageable = cardTypes.getPageable();
        List<CardTypeGetDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}
