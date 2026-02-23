package com.my.service.comms_service.mapper;

import com.my.service.comms_service.model.NotificationAction;
import com.my.service.comms_service.model.Notification;

import com.my.service.comms_service.dto.request.NotificationActionRequestDTO;
import com.my.service.comms_service.dto.response.NotificationActionResponseDTO;
import org.springframework.stereotype.Component;


@Component
public class NotificationActionMapper {

public NotificationActionResponseDTO toDto(NotificationAction entity) {
    if (entity == null) return null;

    NotificationActionResponseDTO dto = new NotificationActionResponseDTO();
    dto.setId(entity.getId());
    dto.setNotificationId(entity.getNotificationId()); // from @JsonIgnore getter
    dto.setUserId(entity.getUserId());
    dto.setAction(entity.getAction());
    dto.setApiCalled(entity.getApiCalled());
    dto.setResult(entity.getResult());
    dto.setActedAt(entity.getActedAt());

    return dto;
}


public NotificationAction toEntity(NotificationActionRequestDTO dto) {
    if (dto == null) return null;

    NotificationAction entity = new NotificationAction();

    // Attach Notification by ID (no DB fetch)
    Notification notification = new Notification();
    notification.setId(dto.getNotificationId());
    entity.setNotification(notification);

    entity.setUserId(dto.getUserId());
    entity.setAction(dto.getAction());
    entity.setApiCalled(dto.getApiCalled());
    entity.setResult(dto.getResult());

    // actedAt → UPDATE on action call
    // if (dto.getActedAt() != null) {
    //     entity.setActedAt(dto.getActedAt());
    // }

    return entity;
}

}
