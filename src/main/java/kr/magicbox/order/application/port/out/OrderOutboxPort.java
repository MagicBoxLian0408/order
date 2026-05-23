package kr.magicbox.order.application.port.out;

import kr.magicbox.order.domain.event.OrderDomainEvent;

public interface OrderOutboxPort {
    void save(OrderDomainEvent event);
}
