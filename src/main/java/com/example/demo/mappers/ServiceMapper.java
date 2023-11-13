package com.example.demo.mappers;

import com.example.demo.entities.Service;
import com.example.demo.payloads.service.ServiceCreateDto;
import com.example.demo.payloads.service.ServiceGetDto;
import com.example.demo.payloads.service.ServiceUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ServiceMapper {
    ServiceMapper SERVICE_MAPPER = Mappers.getMapper(ServiceMapper.class);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "serviceType",ignore = true)
    @Mapping(target = "enable",ignore = true)
    @Mapping(target = "locations",ignore = true)
    Service toEntity(ServiceCreateDto dto);

    @Mapping(target = "serviceType",ignore = true)
    @Mapping(target = "enable",ignore = true)
    @Mapping(target = "locations",ignore = true)
    Service toEntity(ServiceUpdateDto dto);

    @Mapping(target = "serviceTypeId", ignore = true)
    @Mapping(target = "locations", ignore = true)
    ServiceGetDto toDto(Service service);

    default Page<ServiceGetDto> toDto(Page<Service> services){
        if (services==null || services.isEmpty()) {
            return Page.empty();
        }
        List<Service> content = services.getContent();
        Pageable pageable = services.getPageable();
        List<ServiceGetDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}
