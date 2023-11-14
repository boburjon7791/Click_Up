package com.example.demo.mappers;

import com.example.demo.entities.AuthUser;
import com.example.demo.payloads.auth_user.AuthUserCreateDto;
import com.example.demo.payloads.auth_user.AuthUserGetDto;
import com.example.demo.payloads.auth_user.AuthUserUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface AuthUserMapper {
    AuthUserMapper AUTH_USER_MAPPER = Mappers.getMapper(AuthUserMapper.class);
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "confirmed",ignore = true)
    @Mapping(target = "active",ignore = true)
    @Mapping(target = "roles",ignore = true)
    @Mapping(target = "mainCard",ignore = true)
    @Mapping(target = "profileImage",ignore = true)
    @Mapping(target = "cards",ignore = true)
    @Mapping(target = "pinCode",ignore = true)
    @Mapping(target = "initialized",ignore = true)
    @Mapping(target = "code", ignore = true)
    AuthUser toEntity(AuthUserCreateDto dto);

    @Mapping(target = "confirmed",ignore = true)
    @Mapping(target = "active",ignore = true)
    @Mapping(target = "roles",ignore = true)
    @Mapping(target = "mainCard",ignore = true)
    @Mapping(target = "profileImage",ignore = true)
    @Mapping(target = "cards",ignore = true)
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "pinCode",ignore = true)
    @Mapping(target = "initialized",ignore = true)
    @Mapping(target = "code", ignore = true)
    AuthUser toEntity(AuthUserUpdateDto dto);

    @Mapping(target = "mainCard",ignore = true)
    @Mapping(target = "cards",ignore = true)
    @Mapping(target = "roles",ignore = true)
    @Mapping(target = "profileImage",ignore = true)
    AuthUserGetDto toDto(AuthUser user);

    default Page<AuthUserGetDto> toDto(Page<AuthUser> authUsers){
        if (authUsers==null || authUsers.isEmpty()) {
            return Page.empty();
        }
        List<AuthUser> content = authUsers.getContent();
        Pageable pageable = authUsers.getPageable();
        List<AuthUserGetDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}
