package com.example.demo.service;

import com.example.demo.payloads.service_type.ServiceTypeCreateDto;
import com.example.demo.payloads.service_type.ServiceTypeGetDto;
import com.example.demo.payloads.service_type.ServiceTypeUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ServiceTypeCreatorService {
    ServiceTypeGetDto create(ServiceTypeCreateDto dto);
    ServiceTypeGetDto update(ServiceTypeUpdateDto dto);
    ServiceTypeGetDto get(Long id);
    void enable(Long id);
    void disable(Long id);
    Page<ServiceTypeGetDto> serviceTypes(Pageable pageable);
}
