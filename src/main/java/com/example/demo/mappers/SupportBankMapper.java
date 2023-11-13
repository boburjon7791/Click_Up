package com.example.demo.mappers;

import com.example.demo.entities.SupportBank;
import com.example.demo.payloads.bank.SupportBankCreateDto;
import com.example.demo.payloads.bank.SupportBankGetDto;
import com.example.demo.payloads.bank.SupportBankUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface SupportBankMapper {
    SupportBankMapper SUPPORT_BANK_MAPPER = Mappers.getMapper(SupportBankMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enable", ignore = true)
    SupportBank toEntity(SupportBankCreateDto dto);
    
    @Mapping(target = "enable", ignore = true)
    SupportBank toEntity(SupportBankUpdateDto dto);

    SupportBankGetDto toDto(SupportBank bank);


    default Page<SupportBankGetDto> toDto(Page<SupportBank> banks){
        if (banks==null || banks.isEmpty()) {
            return Page.empty();
        }
        List<SupportBank> content = banks.getContent();
        Pageable pageable = banks.getPageable();
        List<SupportBankGetDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}
