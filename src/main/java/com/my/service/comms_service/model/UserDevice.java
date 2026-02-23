package com.my.service.comms_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import com.my.service.comms_service.util.enums.DeviceType;
import java.util.UUID;


// import com.my.service.comms_service.util.enums;
/**
 * UserDevice Model
 * Matches DBML:
 * - device_id PK
 * - user_id FK
 * - device_token
 * - device_type
 * - active
 * - app_version
 * - created_at / updated_at managed automatically
 */
@Data
@Entity
@Table(name = "user_devices", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "device_token" })
})
public class UserDevice  {

    /** Primary Key (device_id) */
    @Id
    @Column(name = "device_id", columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** User associated with this device */
    @Column(name = "user_id", columnDefinition = "uuid", nullable = false)
    private UUID userId;

    /** FCM token */
    @NotBlank
    @Column(name = "device_token", nullable = false, columnDefinition = "text")
    private String deviceToken;

    /** ANDROID / IOS / WEB */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false, length = 20)
    private DeviceType deviceType;

    /** Token validity */
    @Column(name = "active", nullable = false)
    private boolean active = true;

    /** Optional app version string */
    @Column(name = "app_version", columnDefinition = "text")
    private String appVersion;

}
