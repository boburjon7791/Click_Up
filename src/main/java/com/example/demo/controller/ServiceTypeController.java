package com.example.demo.controller;

import com.example.demo.payloads.service_type.ServiceTypeCreateDto;
import com.example.demo.payloads.service_type.ServiceTypeGetDto;
import com.example.demo.payloads.service_type.ServiceTypeUpdateDto;
import com.example.demo.service.ServiceTypeCreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
@RequestMapping("/api.service-types")
public class ServiceTypeController {
    private final ServiceTypeCreatorService serviceTypeCreatorService;

    @PostMapping("/create")
    public ResponseEntity<ServiceTypeGetDto> create(@RequestBody ServiceTypeCreateDto dto){
        ServiceTypeGetDto getDto = serviceTypeCreatorService.create(dto);
        return new ResponseEntity<>(getDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ServiceTypeGetDto> update(@RequestBody ServiceTypeUpdateDto dto){
        ServiceTypeGetDto getDto = serviceTypeCreatorService.update(dto);
        return new ResponseEntity<>(getDto, HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{id}")
    public ResponseEntity<ServiceTypeGetDto> get(@PathVariable Long id){
        ServiceTypeGetDto getDto = serviceTypeCreatorService.get(id);
        return ResponseEntity.ok(getDto);
    }
    @PutMapping("/enable/{id}")
    public ResponseEntity<ServiceTypeGetDto> enable(@PathVariable Long id){
        serviceTypeCreatorService.enable(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/disable/{id}")
    public ResponseEntity<ServiceTypeGetDto> disable(@PathVariable Long id){
        serviceTypeCreatorService.disable(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/service-types")
    public ResponseEntity<Page<ServiceTypeGetDto>> getServiceTypes(@RequestParam Integer page){
        Page<ServiceTypeGetDto> getDtoPage = serviceTypeCreatorService.serviceTypes(PageRequest.of(page, 10));
        return ResponseEntity.ok(getDtoPage);
    }
}
