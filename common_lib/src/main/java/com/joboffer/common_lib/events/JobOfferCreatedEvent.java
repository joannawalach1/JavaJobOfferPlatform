package com.joboffer.common_lib.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobOfferCreatedEvent(
        UUID eventId,
        UUID jobOfferId,
        String company,
        String title,
        String salary,
        String link,
        LocalDateTime createdAt
) {
    public static JobOfferCreatedEvent JobOfferCreatedEvent(
            UUID jobOfferId, String company, String title,
            String salary, String link
    ) {
        return new JobOfferCreatedEvent(
                UUID.randomUUID(),
                jobOfferId,
                company,
                title,
                salary,
                link,
                LocalDateTime.now()
        );
    }
}
