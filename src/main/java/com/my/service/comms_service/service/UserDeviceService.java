package com.my.service.comms_service.service;

import com.my.service.comms_service.model.UserDevice;
import com.my.service.comms_service.repository.IUserDeviceRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserDeviceService {

    private final IUserDeviceRepository userDeviceRepository;

    public UserDevice create(UserDevice userDevice) {
        return userDeviceRepository.save(userDevice);
    }

    public UserDevice update(UUID id, UserDevice userDevice) {

        UserDevice existing = userDeviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserDevice not found with id: " + id));

        // Update only mutable fields
        if (userDevice.getDeviceToken() != null && !userDevice.getDeviceToken().isBlank()) {
            existing.setDeviceToken(userDevice.getDeviceToken());
        }

        if (userDevice.getDeviceType() != null) {
            existing.setDeviceType(userDevice.getDeviceType());
        }

        // Boolean – always update
        existing.setActive(userDevice.isActive());

        if (userDevice.getAppVersion() != null) {
            existing.setAppVersion(userDevice.getAppVersion());
        }

        return userDeviceRepository.save(existing);
    }

    public UserDevice replace(UUID id, UserDevice userDevice) {
        UUID uuid = id;
        if (!userDeviceRepository.existsById(uuid)) {
            throw new EntityNotFoundException("UserDevice not found with id: " + id);
        }
        userDevice.setId(uuid);
        return userDeviceRepository.save(userDevice);
    }

    public void delete(UUID id) {
        UUID uuid = id;
        userDeviceRepository.deleteById(uuid);
    }

    public Optional<UserDevice> findById(UUID id) {
        return userDeviceRepository.findById(id);
    }

    public interface UserDeviceRepository extends JpaRepository<UserDevice, UUID> {

        Optional<UserDevice> findByUserId(UUID userId);

        // OR if there are multiple devices per user:
        List<UserDevice> findAllByUserId(UUID userId);
    }

    public Optional<UserDevice> findLastUpdatedByUser(UUID userId) {
        return userDeviceRepository.findTopByUserIdAndActiveTrueOrderByUpdatedAtDesc(userId);
    }

}
