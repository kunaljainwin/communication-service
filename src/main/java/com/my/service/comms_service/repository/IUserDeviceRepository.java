package com.my.service.comms_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.my.service.comms_service.model.UserDevice;

@RepositoryRestResource(exported = false)
public interface IUserDeviceRepository extends JpaRepository<UserDevice, UUID> {

    List<UserDevice> findAllByUserId(UUID userId);
    // Get the most recently updated device
    Optional<UserDevice> findTopByUserIdAndActiveTrueOrderByUpdatedAtDesc(UUID userId);

    // OR if column name is different:
    // Optional<UserDevice> findTopByUserIdOrderByLastModifiedDesc(UUID userId);

}
