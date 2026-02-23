package com.my.service.comms_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;
import com.my.service.comms_service.model.Notification;

@RepositoryRestResource(exported = false)
public interface INotificationRepository extends JpaRepository<Notification, UUID> {
    public List<Notification> findAllByUserId(UUID userId);
}
