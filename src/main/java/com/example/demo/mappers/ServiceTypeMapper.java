package com.example.demo.mappers;

import com.example.demo.entities.ServiceType;
import com.example.demo.payloads.service_type.ServiceTypeCreateDto;
import com.example.demo.payloads.service_type.ServiceTypeGetDto;
import com.example.demo.payloads.service_type.ServiceTypeUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ServiceTypeMapper {
    ServiceTypeMapper SERVICE_TYPE_MAPPER = Mappers.getMapper(ServiceTypeMapper.class);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "enable",ignore = true)
    ServiceType toEntity(ServiceTypeCreateDto dto);

    ServiceType toEntity(ServiceTypeUpdateDto dto);

    ServiceTypeGetDto toDto(ServiceType service);

    default Page<ServiceTypeGetDto> toDto(Page<ServiceType> services){
        if (services==null || services.isEmpty()) {
            return Page.empty();
        }
        List<ServiceType> content = services.getContent();
        Pageable pageable = services.getPageable();
        List<ServiceTypeGetDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}
