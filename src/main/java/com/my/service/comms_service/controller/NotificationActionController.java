package com.my.service.comms_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.my.service.comms_service.dto.request.NotificationActionRequestDTO;
import com.my.service.comms_service.dto.response.NotificationActionResponseDTO;
import com.my.service.comms_service.dto.response.ApiResponse;
import com.my.service.comms_service.mapper.NotificationActionMapper;
import com.my.service.comms_service.model.NotificationAction;
import com.my.service.comms_service.service.NotificationActionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/notificationActions")
@RequiredArgsConstructor
public class NotificationActionController {

    private final NotificationActionService notificationActionService;
    private final NotificationActionMapper notificationActionMapper;

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationActionResponseDTO>> getById(@PathVariable UUID id) {
        NotificationAction data = notificationActionService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NotificationAction not found with ID: " + id));
        return ResponseEntity.ok(new ApiResponse<>(200, "NotificationAction fetched", notificationActionMapper.toDto(data)));
    }

    // Create
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationActionResponseDTO>> create(@Valid @RequestBody NotificationActionRequestDTO dto) {
        NotificationAction saved = notificationActionService.create(notificationActionMapper.toEntity(dto));
        return new ResponseEntity<>(new ApiResponse<>(201, "NotificationAction created", notificationActionMapper.toDto(saved)), HttpStatus.CREATED);
    }

    // Patch (partial)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationActionResponseDTO>> patch(@PathVariable UUID id, @RequestBody NotificationActionRequestDTO dto) {
        NotificationAction updated = notificationActionService.update(id, notificationActionMapper.toEntity(dto));
        return ResponseEntity.ok(new ApiResponse<>(200, "NotificationAction updated", notificationActionMapper.toDto(updated)));
    }

    // Put (replace)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationActionResponseDTO>> replace(@PathVariable UUID id, @Valid @RequestBody NotificationActionRequestDTO dto) {
        NotificationAction updated = notificationActionService.replace(id, notificationActionMapper.toEntity(dto));
        return ResponseEntity.ok(new ApiResponse<>(200, "NotificationAction replaced", notificationActionMapper.toDto(updated)));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        notificationActionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
