package com.example.demo.mappers;

import com.example.demo.entities.ConfirmedUser;
import com.example.demo.payloads.auth_user.ConfirmedUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper
public interface ConfirmedUserMapper {
    ConfirmedUserMapper CONFIRMED_USER_MAPPER = Mappers.getMapper(ConfirmedUserMapper.class);

    @Mapping(target = "authUser", ignore = true)
    ConfirmedUser toEntity(ConfirmedUserDto dto);

    @Mapping(target = "id", ignore = true)
    ConfirmedUserDto toDto(ConfirmedUser user);

    default Page<ConfirmedUserDto> toDto(Page<ConfirmedUser> users){
        if (users==null || users.isEmpty()) {
            return Page.empty();
        }
        List<ConfirmedUser> content = users.getContent();
        List<ConfirmedUserDto> dtoList = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(dtoList, users.getPageable(), dtoList.size());
    }
}
