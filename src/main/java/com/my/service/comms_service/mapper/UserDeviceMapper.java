package com.my.service.comms_service.mapper;

import com.my.service.comms_service.model.UserDevice;
import com.my.service.comms_service.dto.request.UserDeviceRequestDTO;
import com.my.service.comms_service.dto.response.UserDeviceResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDeviceMapper {

    public UserDeviceResponseDTO toDto(UserDevice entity) {
        if (entity == null)
            return null;

        UserDeviceResponseDTO dto = new UserDeviceResponseDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setDeviceToken(entity.getDeviceToken());
        dto.setDeviceType(entity.getDeviceType());
        dto.setActive(entity.isActive());
        dto.setAppVersion(entity.getAppVersion());

        return dto;
    }

    public UserDevice toEntity(UserDeviceRequestDTO dto) {
        if (dto == null)
            return null;

        UserDevice entity = new UserDevice();
        entity.setUserId(dto.getUserId());
        entity.setDeviceToken(dto.getDeviceToken());
        entity.setDeviceType(dto.getDeviceType());
        entity.setAppVersion(dto.getAppVersion());

        // default fields
        entity.setActive(true);

        return entity;
    }

    public void updateEntity(UserDevice entity, UserDeviceRequestDTO dto) {
        if (dto == null || entity == null)
            return;

        if (dto.getDeviceToken() != null)
            entity.setDeviceToken(dto.getDeviceToken());

        if (dto.getDeviceType() != null)
            entity.setDeviceType(dto.getDeviceType());

        if (dto.getAppVersion() != null)
            entity.setAppVersion(dto.getAppVersion());
    }

}
