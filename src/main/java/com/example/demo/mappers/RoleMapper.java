package com.example.demo.mappers;

import com.example.demo.entities.Role;
import com.example.demo.payloads.role.RoleCreateDto;
import com.example.demo.payloads.role.RoleGetDto;
import com.example.demo.payloads.role.RoleUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface RoleMapper {
    RoleMapper ROLE_MAPPER = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "authUsers",ignore = true)
    @Mapping(target = "active",ignore = true)
    Role toEntity(RoleCreateDto dto);

    @Mapping(target = "active",ignore = true)
    @Mapping(target = "authUsers",ignore = true)
    Role toEntity(RoleUpdateDto dto);

    RoleGetDto toDto(Role role);

    default Page<RoleGetDto> toDto(Page<Role> roles){
        if (roles==null || roles.isEmpty()) {
            return Page.empty();
        }
        List<Role> content = roles.getContent();
        Pageable pageable = roles.getPageable();
        List<RoleGetDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}
