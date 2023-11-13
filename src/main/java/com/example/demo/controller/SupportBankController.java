package com.example.demo.controller;

import com.example.demo.payloads.bank.SupportBankCreateDto;
import com.example.demo.payloads.bank.SupportBankGetDto;
import com.example.demo.payloads.bank.SupportBankUpdateDto;
import com.example.demo.service.SupportBankService;
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
@RequestMapping("/api.support-bank")
public class SupportBankController {
    private final SupportBankService supportBankService;

    @PostMapping("/create")
    public ResponseEntity<SupportBankGetDto> create(@RequestBody SupportBankCreateDto dto){
        SupportBankGetDto getDto = supportBankService.create(dto);
        return new ResponseEntity<>(getDto, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<SupportBankGetDto> update(@RequestBody SupportBankUpdateDto dto){
        SupportBankGetDto getDto = supportBankService.update(dto);
        return new ResponseEntity<>(getDto, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<SupportBankGetDto> get(@PathVariable Integer id){
        SupportBankGetDto getDto = supportBankService.get(id);
        return ResponseEntity.ok(getDto);
    }
    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enable(@PathVariable Integer id){
        supportBankService.enable(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Integer id){
        supportBankService.disable(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/get-all")
    public ResponseEntity<Page<SupportBankGetDto>> supportBanks(@RequestParam Integer page){
        Page<SupportBankGetDto> supportBanks = supportBankService.supportBanks(PageRequest.of(page, 10));
        return ResponseEntity.ok(supportBanks);
    }
}
