package com.example.demo.controller;

import com.example.demo.payloads.location.LocationCreateDto;
import com.example.demo.payloads.location.LocationGetDto;
import com.example.demo.payloads.location.LocationUpdateDto;
import com.example.demo.service.LocationService;
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
@RequestMapping("/api.location")
public class LocationController {
    private final LocationService locationService;
    @PostMapping("/create")
    public ResponseEntity<LocationGetDto> create(@RequestBody LocationCreateDto dto){
        LocationGetDto getDto = locationService.create(dto);
        return new ResponseEntity<>(getDto, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<LocationGetDto> update(@RequestBody LocationUpdateDto dto){
        LocationGetDto getDto = locationService.update(dto);
        return new ResponseEntity<>(getDto, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LocationGetDto> get(@PathVariable Long id){
        LocationGetDto getDto = locationService.get(id);
        return ResponseEntity.ok(getDto);
    }
    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enable(@PathVariable Long id){
        locationService.enable(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Long id){
        locationService.disable(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/all")
    public ResponseEntity<Page<LocationGetDto>> getAll(@RequestParam Integer page){
        Page<LocationGetDto> locations = locationService.locations(PageRequest.of(page, 10));
        return ResponseEntity.ok(locations);
    }
}
