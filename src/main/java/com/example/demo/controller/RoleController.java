package com.example.demo.controller;

import com.example.demo.payloads.role.RoleCreateDto;
import com.example.demo.payloads.role.RoleGetDto;
import com.example.demo.payloads.role.RoleUpdateDto;
import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
@RequestMapping("/api.role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<RoleGetDto> create(@RequestBody RoleCreateDto dto){
        RoleGetDto getDto = roleService.create(dto);
        return new ResponseEntity<>(getDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<RoleGetDto> update(@RequestBody RoleUpdateDto dto){
        RoleGetDto getDto = roleService.update(dto);
        return new ResponseEntity<>(getDto, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{id}")
    public ResponseEntity<RoleGetDto> get(@PathVariable Integer id){
        return ResponseEntity.ok(roleService.get(id));
    }
    @PutMapping("/enable/{id}")
    public ResponseEntity<Void> enable(@PathVariable Integer id){
        roleService.enable(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Integer id){
        roleService.disable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<RoleGetDto>> getAll(@RequestParam Integer page){
        return ResponseEntity.ok(roleService.roles(PageRequest.of(page,10)));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/roles-of-user/{userId}")
    public ResponseEntity<List<RoleGetDto>> getRolesOfUser(@PathVariable Long userId){
        return ResponseEntity.ok(roleService.rolesOfUsers(userId));
    }
}
