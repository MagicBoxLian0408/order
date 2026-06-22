package kr.magicbox.order.adapter.out.persistence;

import tools.jackson.databind.ObjectMapper;
import kr.magicbox.order.adapter.out.persistence.entity.OrderOutboxEntity;
import kr.magicbox.order.adapter.out.persistence.repository.OrderOutboxJpaRepository;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.domain.event.OrderDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderOutboxAdapter implements OrderOutboxPort {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(OrderDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        orderOutboxJpaRepository.save(OrderOutboxEntity.builder()
                .eventType(event.eventType().getValue())
                .payload(payload)
                .orderId(event.orderId())
                .orderLineId(event.orderLineId())
                .build());
    }
}
