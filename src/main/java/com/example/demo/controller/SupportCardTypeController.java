package com.example.demo.controller;

import com.example.demo.payloads.card_type.CardTypeCreateDto;
import com.example.demo.payloads.card_type.CardTypeGetDto;
import com.example.demo.payloads.card_type.CardTypeUpdateDto;
import com.example.demo.service.SupportCardTypeService;
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
@RequestMapping("/api.support-card-type")
public class SupportCardTypeController {
    private final SupportCardTypeService supportCardTypeService;

    @PostMapping("/create")
    public ResponseEntity<CardTypeGetDto> create(@RequestBody CardTypeCreateDto dto){
        CardTypeGetDto getDto = supportCardTypeService.create(dto);
        return new ResponseEntity<>(getDto, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<CardTypeGetDto> update(@RequestBody CardTypeUpdateDto dto){
        CardTypeGetDto getDto = supportCardTypeService.update(dto);
        return new ResponseEntity<>(getDto, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CardTypeGetDto> get(@PathVariable Integer id){
        CardTypeGetDto getDto = supportCardTypeService.get(id);
        return ResponseEntity.ok(getDto);
    }
    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enable(@PathVariable Integer id){
        supportCardTypeService.enable(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Integer id){
        supportCardTypeService.disable(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-all-cards")
    public ResponseEntity<Page<CardTypeGetDto>> getAll(@RequestParam Integer page){
        Page<CardTypeGetDto> cardTypes = supportCardTypeService.cardTypes(PageRequest.of(page, 10));
        return ResponseEntity.ok(cardTypes);
    }
}
