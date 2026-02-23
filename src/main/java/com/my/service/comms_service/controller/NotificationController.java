package com.my.service.comms_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.my.service.comms_service.dto.request.NotificationRequestDTO;
import com.my.service.comms_service.dto.response.NotificationResponseDTO;
import com.my.service.comms_service.dto.response.ApiResponse;
import com.my.service.comms_service.mapper.NotificationMapper;
import com.my.service.comms_service.model.Notification;
import com.my.service.comms_service.service.NotificationService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import com.my.service.comms_service.dto.request.ActionRequest;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> getById(@PathVariable UUID id) {
        Notification data = notificationService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with ID: " + id));
        return ResponseEntity.ok(new ApiResponse<>(200, "Notification fetched", notificationMapper.toDto(data)));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> getAllByUserId(@RequestParam UUID userId) {
        List<NotificationResponseDTO> dataList = notificationService.getAllByUserId(userId).stream()
            .map(notificationMapper::toDto)
            .collect(Collectors.toList());

            if (dataList.isEmpty()) {
                throw new EntityNotFoundException("No notifications found for user: " + userId);
            }
        
            return ResponseEntity.ok(new ApiResponse<>(200, "Notification fetched", dataList));

    }

    // // Create
    // @PostMapping
    // public ResponseEntity<ApiResponse<NotificationResponseDTO>> create(@Valid @RequestBody NotificationRequestDTO dto) {
    //     Notification saved = notificationService.create(notificationMapper.toEntity(dto));
    //     return new ResponseEntity<>(new ApiResponse<>(201, "Notification created", notificationMapper.toDto(saved)),
    //             HttpStatus.CREATED);
    // }

    // Patch (partial)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> patch(@PathVariable UUID id,
            @RequestBody NotificationRequestDTO dto) {
        Notification updated = notificationService.update(id, notificationMapper.toEntity(dto));
        return ResponseEntity.ok(new ApiResponse<>(200, "Notification updated", notificationMapper.toDto(updated)));
    }

    // Put (replace)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> replace(@PathVariable UUID id,
            @Valid @RequestBody NotificationRequestDTO dto) {
        Notification updated = notificationService.update(id, notificationMapper.toEntity(dto));
        return ResponseEntity.ok(new ApiResponse<>(200, "Notification replaced", notificationMapper.toDto(updated)));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/action") 
    public ResponseEntity<Void> handleAction(
            @PathVariable UUID id,
            @RequestBody ActionRequest request
    ) {
        notificationService.performAction(id,request);
        return ResponseEntity.ok().build();
    }
}
