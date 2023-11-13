package com.example.demo.service;

import com.example.demo.payloads.role.RoleCreateDto;
import com.example.demo.payloads.role.RoleGetDto;
import com.example.demo.payloads.role.RoleUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface RoleService {
    RoleGetDto create(RoleCreateDto dto);
    RoleGetDto update(RoleUpdateDto dto);
    RoleGetDto get(Integer id);
    void enable(Integer id);
    void disable(Integer id);
    Page<RoleGetDto> roles(Pageable pageable);
    List<RoleGetDto> rolesOfUsers(Long id);
}
