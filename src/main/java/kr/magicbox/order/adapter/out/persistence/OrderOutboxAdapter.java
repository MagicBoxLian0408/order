package kr.magicbox.order.adapter.out.persistence;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;
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
        OrderOutboxEntity entity = orderOutboxJpaRepository.save(OrderOutboxEntity.builder()
                .eventType(event.eventType().getValue())
                .payload(payload)
                .build());

        // event_id(= outbox row id)를 payload에 주입 — orchestrator IdempotentAspect가 이 값을 사용
        ObjectNode node = (ObjectNode) objectMapper.readTree(payload);
        node.put("event_id", entity.getId());
        entity.updatePayload(objectMapper.writeValueAsString(node));
    }
}
