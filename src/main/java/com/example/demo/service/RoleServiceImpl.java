package com.example.demo.service;

import com.example.demo.entities.Role;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.payloads.role.RoleCreateDto;
import com.example.demo.payloads.role.RoleGetDto;
import com.example.demo.payloads.role.RoleUpdateDto;
import com.example.demo.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.example.demo.mappers.RoleMapper.ROLE_MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public RoleGetDto create(RoleCreateDto dto) {
        if (roleRepository.existsByName(dto.name)) {
            throw new BadRequestException("This role's name already exist");
        }
        Role role = ROLE_MAPPER.toEntity(dto);
        Role saved = roleRepository.save(role);
        log.info("{} created",saved);
        return ROLE_MAPPER.toDto(saved);
    }

    @Override
    public RoleGetDto update(RoleUpdateDto dto) {
        if (!roleRepository.existsByName(dto.name)) {
            throw new NotFoundException("This role not found by this name");
        }
        if (!roleRepository.existsById(dto.id)) {
            throw new NotFoundException("This role is not exist");
        }
        Role role = ROLE_MAPPER.toEntity(dto);
        Role updated = roleRepository.save(role);
        log.info("{} updated",updated);
        return ROLE_MAPPER.toDto(updated);
    }

    @Override
    public RoleGetDto get(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("This role is not exists"));
        log.info("{} was gave",role);
        return ROLE_MAPPER.toDto(role);
    }

    @Override
    public void enable(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException("This role is not exist");
        }
        roleRepository.updateActiveTrueById(id);
    }

    @Override
    public void disable(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException("This role is not exist");
        }
        roleRepository.updateActiveFalseById(id);
    }

    @Override
    public Page<RoleGetDto> roles(Pageable pageable) {
        Page<Role> all = roleRepository.findAll(pageable);
        log.info("gave all roles");
        return ROLE_MAPPER.toDto(all);
    }

    @Override
    public List<RoleGetDto> rolesOfUsers(Long id) {
        List<Role> roles = roleRepository.findAllByUserId(id);
        log.info("gave all roles of {} user",id);
        return roles.stream()
                .map(ROLE_MAPPER::toDto)
                .toList();
    }
}
