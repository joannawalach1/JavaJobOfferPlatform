package com.joboffer.common_lib.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        UUID userId,
        String email,
        String firstName,
        String lastName,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt) {

    public static UserRegisteredEvent createEvent(
            UUID userId, String email,
            String firstName, String lastName,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new UserRegisteredEvent(
                UUID.randomUUID(),
                userId,
                email,
                firstName,
                lastName,
                createdAt,
                updatedAt
        );
    }
}
