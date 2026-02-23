package com.my.service.comms_service.util.enums;

public enum NotificationStatus {
    CREATED,
    PENDING,
    READ,
    ACTED;

    public boolean canTransitionTo(NotificationStatus next) {
        return this.ordinal() < next.ordinal();
    }
}