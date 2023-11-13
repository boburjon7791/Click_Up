package com.example.demo.service;

import com.example.demo.payloads.service.ServiceCreateDto;
import com.example.demo.payloads.service.ServiceGetDto;
import com.example.demo.payloads.service.ServiceUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ServiceCreatorService {
    ServiceGetDto create(ServiceCreateDto dto);
    ServiceGetDto update(ServiceUpdateDto dto);
    ServiceGetDto get(Long id);
    void enable(Long id);
    void disable(Long id);
    Page<ServiceGetDto> services(Pageable pageable);
}
