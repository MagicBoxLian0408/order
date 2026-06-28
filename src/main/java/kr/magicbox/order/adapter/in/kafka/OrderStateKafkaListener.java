package kr.magicbox.order.adapter.in.kafka;

import kr.magicbox.order.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.order.adapter.in.kafka.event.OrderPrepareConfirmedEvent;
import kr.magicbox.order.adapter.out.persistence.entity.OrderInboxEntity;
import kr.magicbox.order.adapter.out.persistence.repository.OrderInboxJpaRepository;
import kr.magicbox.order.application.port.in.HandleOrderPrepareUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStateKafkaListener {

    private final HandleOrderPrepareUseCase handleOrderPrepareUseCase;
    private final OrderInboxJpaRepository orderInboxJpaRepository;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.order-prepare-confirmed", groupId = "order-service")
    public void handleOrderPrepareConfirmed(ConsumerRecord<String, OrderPrepareConfirmedEvent> consumerRecord) {
        log.info("[Inbox] order.prepare.confirmed 이벤트 수신. orderId={}", consumerRecord.value().orderId());
        handleOrderPrepareUseCase.handleOrderPrepare(consumerRecord.value().orderId());
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        orderInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(OrderInboxEntity::markDeadLettered);
    }
}
