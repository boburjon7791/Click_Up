package com.example.demo.service;

import com.example.demo.payloads.location.LocationCreateDto;
import com.example.demo.payloads.location.LocationGetDto;
import com.example.demo.payloads.location.LocationUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface LocationService {
    LocationGetDto create(LocationCreateDto dto);
    LocationGetDto update(LocationUpdateDto dto);
    LocationGetDto get(Long id);
    void enable(Long id);
    void disable(Long id);
    Page<LocationGetDto> locations(Pageable pageable);
}
