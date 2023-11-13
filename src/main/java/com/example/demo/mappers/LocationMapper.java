package com.example.demo.mappers;

import com.example.demo.entities.Location;
import com.example.demo.payloads.location.LocationCreateDto;
import com.example.demo.payloads.location.LocationGetDto;
import com.example.demo.payloads.location.LocationUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface LocationMapper {
    LocationMapper LOCATION_MAPPER = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "services",ignore = true)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "active",ignore = true)
    Location toEntity(LocationCreateDto dto);

    @Mapping(target = "active",ignore = true)
    @Mapping(target = "services",ignore = true)
    Location toEntity(LocationUpdateDto dto);

    LocationGetDto toDto(Location location);

    default Page<LocationGetDto> toDto(Page<Location> locations){
        if (locations==null || locations.isEmpty()) {
            return Page.empty();
        }
        List<Location> content = locations.getContent();
        Pageable pageable = locations.getPageable();
        List<LocationGetDto> list = content.stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(list, pageable, list.size());
    }
}
