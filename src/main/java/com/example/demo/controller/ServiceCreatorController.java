package com.example.demo.controller;

import com.example.demo.payloads.service.ServiceCreateDto;
import com.example.demo.payloads.service.ServiceGetDto;
import com.example.demo.payloads.service.ServiceUpdateDto;
import com.example.demo.service.ServiceCreatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
@RequestMapping("/api.services")
public class ServiceCreatorController {
    private final ServiceCreatorService serviceCreatorService;

    @PostMapping("/create")
    public ResponseEntity<ServiceGetDto> create(@RequestBody @Valid ServiceCreateDto dto){
        ServiceGetDto getDto = serviceCreatorService.create(dto);
        return new ResponseEntity<>(getDto, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<ServiceGetDto> update(@RequestBody @Valid ServiceUpdateDto dto){
        ServiceGetDto getDto = serviceCreatorService.update(dto);
        return new ResponseEntity<>(getDto, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServiceGetDto> get(@PathVariable Long id){
        ServiceGetDto getDto = serviceCreatorService.get(id);
        return ResponseEntity.ok(getDto);
    }
    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enable(@PathVariable Long id){
        serviceCreatorService.enable(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Long id){
        serviceCreatorService.disable(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/all")
    public ResponseEntity<Page<ServiceGetDto>> getAll(@RequestParam Integer page){
        Page<ServiceGetDto> services = serviceCreatorService.services(PageRequest.of(page, 10));
        return ResponseEntity.ok(services);
    }
}
