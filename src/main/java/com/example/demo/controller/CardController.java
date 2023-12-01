package com.example.demo.controller;

import com.example.demo.exceptions.ForbiddenAccessException;
import com.example.demo.payloads.card.CardCreateDto;
import com.example.demo.payloads.card.CardGetDto;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.repositories.SupportBankRepository;
import com.example.demo.repositories.SupportCardTypeRepository;
import com.example.demo.service.CardService;
import com.example.demo.utils.JwtTokenUtils;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.utils.JwtTokenUtils.getEmail;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
@RequestMapping("/api.card")
public class CardController {
    private final CardService cardService;
    private final AuthUserRepository authUserRepository;
    private final SupportBankRepository supportBankRepository;
    private final SupportCardTypeRepository supportCardTypeRepository;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CardGetDto> create(@RequestBody @Valid CardCreateDto dto,
                                             HttpServletRequest request){
        CardGetDto getDto = cardService.create(dto,request);
        return new ResponseEntity<>(getDto, HttpStatus.CREATED);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{numberBase64}")
    public ResponseEntity<CardGetDto> get(@PathVariable String numberBase64){
        CardGetDto getDto = cardService.get(decode(numberBase64));
        return ResponseEntity.ok(getDto);
    }
    @PutMapping("/enable/{numberBase64}")
    public ResponseEntity<Void> enable(@PathVariable String numberBase64){
        cardService.enable(decode(numberBase64));
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/admin/disable/{numberBase64}")
    public ResponseEntity<Void> adminDisable(@PathVariable String numberBase64){
        cardService.disable(decode(numberBase64));
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/disable/{numberBase64}")
    public ResponseEntity<Void> disable(@PathVariable String numberBase64, HttpServletRequest request){
        String decoded = decode(numberBase64);
        String email = getEmail(request.getHeader("Authorization"));
        if(!authUserRepository.existsThisCardInUserAndUserActiveTrue(email, decoded)){
            throw new ForbiddenAccessException("You can not disable this %s card".formatted(decoded));
        }
        cardService.disable(decoded);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/cards")
    public ResponseEntity<Page<CardGetDto>> cards(@RequestParam Integer page){
        Page<CardGetDto> cards = cardService.cards(PageRequest.of(page, 10));
        return ResponseEntity.ok(cards);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-cards/{userId}")
    public ResponseEntity<List<CardGetDto>> myCards(@PathVariable Long userId){
        List<CardGetDto> getDtoList = cardService.myCards(userId);
        return ResponseEntity.ok(getDtoList);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/exist-bank/{bank}")
    public ResponseEntity<Boolean> existsBank(@PathVariable String bank){
        return ResponseEntity.ok(supportBankRepository.existsByNameAndEnableTrue(bank));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/exists/card-type/{cardType}")
    public ResponseEntity<Boolean> existsCardType(@PathVariable String cardType){
        return ResponseEntity.ok(supportCardTypeRepository.existsByNameAndEnableTrue(cardType));
    }
    public static String decode(String base64){
        byte[] decode = Decoders.BASE64.decode(base64);
        StringBuilder sb = new StringBuilder();
        for (byte b : decode) {
            sb.append((char) b);
        }
        return sb.toString();
    }
}
