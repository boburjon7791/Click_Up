package com.example.demo.service;

import com.example.demo.entities.Location;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.location.LocationCreateDto;
import com.example.demo.payloads.location.LocationGetDto;
import com.example.demo.payloads.location.LocationUpdateDto;
import com.example.demo.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.mappers.LocationMapper.LOCATION_MAPPER;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public LocationGetDto create(LocationCreateDto dto) {
        Location location = LOCATION_MAPPER.toEntity(dto);
        Location saved = locationRepository.save(location);
        log.info("{} saved",saved);
        return LOCATION_MAPPER.toDto(saved);
    }

    @Override
    public LocationGetDto update(LocationUpdateDto dto) {
        if (!locationRepository.existsById(dto.id)) {
            throw new NotFoundException("Location is not exist!");
        }
        Location location = LOCATION_MAPPER.toEntity(dto);
        Location updated = locationRepository.save(location);
        log.info("{} updated",updated);
        return LOCATION_MAPPER.toDto(updated);
    }

    @Override
    public LocationGetDto get(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location is not exist!"));
        log.info("{} gave",location);
        return LOCATION_MAPPER.toDto(location);
    }

    @Override
    public void enable(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new NotFoundException("Location is not exist");
        }
        locationRepository.updateActiveTrueById(id);
    }

    @Override
    public void disable(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new NotFoundException("Location is not exist");
        }
        locationRepository.updateActiveFalseById(id);
    }

    @Override
    public Page<LocationGetDto> locations(Pageable pageable) {
        Page<Location> all = locationRepository.findAll(pageable);
        log.info("{} elements gave",all.getTotalElements());
        return LOCATION_MAPPER.toDto(all);
    }
}
