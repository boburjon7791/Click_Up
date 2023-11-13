package com.example.demo.service;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.service.ServiceCreateDto;
import com.example.demo.payloads.service.ServiceGetDto;
import com.example.demo.payloads.service.ServiceUpdateDto;
import com.example.demo.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.mappers.ServiceMapper.SERVICE_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceCreatorServiceImpl implements ServiceCreatorService {
    private final ServiceRepository serviceRepository;

    @Override
    public ServiceGetDto create(ServiceCreateDto dto) {
        if (serviceRepository.existsByNameOrRestApiOrBankAccount(dto.name, dto.restApi, dto.bankAccount)) {
            throw new BadRequestException("This service already exist");
        }
        com.example.demo.entities.Service service = SERVICE_MAPPER.toEntity(dto);
        com.example.demo.entities.Service saved = serviceRepository.save(service);
        log.info("{} saved",saved);
        return SERVICE_MAPPER.toDto(saved);
    }

    @Override
    public ServiceGetDto update(ServiceUpdateDto dto) {
        if (!serviceRepository.existsById(dto.id)) {
            throw new NotFoundException("This service is not exist");
        }
        com.example.demo.entities.Service service = SERVICE_MAPPER.toEntity(dto);
        com.example.demo.entities.Service updated = serviceRepository.save(service);
        log.info("{} updated",updated);
        return SERVICE_MAPPER.toDto(updated);
    }

    @Override
    public ServiceGetDto get(Long id) {
        com.example.demo.entities.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("This service is not exist"));
        log.info("{} gave",service);
        return SERVICE_MAPPER.toDto(service);
    }

    @Override
    public void enable(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new NotFoundException("This service is not exist");
        }
        serviceRepository.updateEnableTrueById(id);
    }

    @Override
    public void disable(Long id) {
        if (!serviceRepository.existsById(id)) {
            throw new NotFoundException("This service is not exist");
        }
        serviceRepository.updateEnableFalseById(id);
    }

    @Override
    public Page<ServiceGetDto> services(Pageable pageable) {
        Page<com.example.demo.entities.Service> all = serviceRepository.findAll(pageable);
        log.info("{} elements gave",all.getTotalElements());
        return SERVICE_MAPPER.toDto(all);
    }
}
