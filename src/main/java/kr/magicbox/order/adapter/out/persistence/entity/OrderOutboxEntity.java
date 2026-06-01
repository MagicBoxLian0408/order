package kr.magicbox.order.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_outbox")
public class OrderOutboxEntity extends BaseEntity {

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "JSON")
    private String payload;

    @Builder
    public OrderOutboxEntity(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    public void updatePayload(String payload) {
        this.payload = payload;
    }
}
