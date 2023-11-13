package com.example.demo.service;

import com.example.demo.entities.ServiceType;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.service_type.ServiceTypeCreateDto;
import com.example.demo.payloads.service_type.ServiceTypeGetDto;
import com.example.demo.payloads.service_type.ServiceTypeUpdateDto;
import com.example.demo.repositories.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.demo.mappers.ServiceTypeMapper.SERVICE_TYPE_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceTypeCreatorServiceImpl implements ServiceTypeCreatorService {
    private final ServiceTypeRepository serviceTypeRepository;
    @Override
    public ServiceTypeGetDto create(ServiceTypeCreateDto dto) {
        if(serviceTypeRepository.existsByName(dto.name)){
            throw new BadRequestException("This service name already exist");
        }
        ServiceType serviceType = SERVICE_TYPE_MAPPER.toEntity(dto);
        ServiceType saved = serviceTypeRepository.save(serviceType);
        log.info("{} saved",saved);
        return SERVICE_TYPE_MAPPER.toDto(saved);
    }

    @Override
    public ServiceTypeGetDto update(ServiceTypeUpdateDto dto) {
        if (!serviceTypeRepository.existsById(dto.id)) {
            throw new NotFoundException("This service is not exist");
        }
        if (serviceTypeRepository.existsByName(dto.name)) {
            throw new BadRequestException("This service name already exist");
        }
        ServiceType serviceType = SERVICE_TYPE_MAPPER.toEntity(dto);
        ServiceType updated = serviceTypeRepository.save(serviceType);
        log.info("{} updated",updated);
        return SERVICE_TYPE_MAPPER.toDto(updated);
    }

    @Override
    public ServiceTypeGetDto get(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("This service is not exist"));
        log.info("{} gave",serviceType);
        return SERVICE_TYPE_MAPPER.toDto(serviceType);
    }

    @Override
    public void enable(Long id) {
        if (!serviceTypeRepository.existsById(id)) {
            throw new NotFoundException("This service is not exist");
        }
        serviceTypeRepository.updateEnableTrueById(id);
    }

    @Override
    public void disable(Long id) {
        if (!serviceTypeRepository.existsById(id)) {
            throw new NotFoundException("This service is not exist");
        }
        serviceTypeRepository.updateEnableFalseById(id);
    }

    @Override
    public Page<ServiceTypeGetDto> serviceTypes(Pageable pageable) {
        Page<ServiceType> all = serviceTypeRepository.findAll(pageable);
        return SERVICE_TYPE_MAPPER.toDto(all);
    }
}
