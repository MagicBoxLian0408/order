package kr.magicbox.order.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReleaseSoldQuantityIncreaseEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("release_id") Long releaseId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements OrderDomainEvent {

    public static ReleaseSoldQuantityIncreaseEvent of(Long releaseId) {
        return ReleaseSoldQuantityIncreaseEvent.builder()
                .eventId(releaseId)
                .releaseId(releaseId)
                .occurredAt(Instant.now())
                .build();
    }

    @Override
    public String key() {
        return releaseId.toString();
    }

    @Override
    public OrderDomainEventType eventType() {
        return OrderDomainEventType.RELEASE_SOLD_QUANTITY_INCREASE;
    }
}
