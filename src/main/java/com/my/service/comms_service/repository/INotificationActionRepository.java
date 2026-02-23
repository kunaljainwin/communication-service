package com.my.service.comms_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;
import com.my.service.comms_service.model.NotificationAction;

@RepositoryRestResource(exported = false)
public interface INotificationActionRepository extends JpaRepository<NotificationAction, UUID> {
}
