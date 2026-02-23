package com.my.service.comms_service.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.my.service.comms_service.dto.request.UserDeviceRequestDTO;
import com.my.service.comms_service.dto.response.UserDeviceResponseDTO;
import com.my.service.comms_service.dto.response.ApiResponse;
import com.my.service.comms_service.mapper.UserDeviceMapper;
import com.my.service.comms_service.model.UserDevice;
import com.my.service.comms_service.service.UserDeviceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class UserDeviceController {
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private UserDeviceMapper userDeviceMapper;

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDeviceResponseDTO>> getById(@PathVariable UUID id) {
        UserDevice data = userDeviceService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserDevice not found with ID: " + id));
        return ResponseEntity.ok(new ApiResponse<>(200, "UserDevice fetched", userDeviceMapper.toDto(data)));
    }

    // Create
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDeviceResponseDTO>> create(@Valid @RequestBody UserDeviceRequestDTO dto) {
        UserDevice saved = userDeviceService.create(userDeviceMapper.toEntity(dto));
        return new ResponseEntity<>(new ApiResponse<>(201, "UserDevice created", userDeviceMapper.toDto(saved)), HttpStatus.CREATED);
    }

    // Patch (partial)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDeviceResponseDTO>> patch(@PathVariable UUID id, @RequestBody UserDeviceRequestDTO dto) {
        UserDevice updated = userDeviceService.update(id, userDeviceMapper.toEntity(dto));
        return ResponseEntity.ok(new ApiResponse<>(200, "UserDevice updated", userDeviceMapper.toDto(updated)));
    }

    // Put (replace)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDeviceResponseDTO>> replace(@PathVariable UUID id, @Valid @RequestBody UserDeviceRequestDTO dto) {
        UserDevice updated = userDeviceService.replace(id, userDeviceMapper.toEntity(dto));
        return ResponseEntity.ok(new ApiResponse<>(200, "UserDevice replaced", userDeviceMapper.toDto(updated)));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userDeviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
