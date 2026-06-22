package kr.magicbox.order.domain.event;

public interface OrderDomainEvent {
    String key();
    OrderDomainEventType eventType();
    Long orderId();

    default Long orderLineId() {
        return null;
    }
}
