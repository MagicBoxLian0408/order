package kr.magicbox.order.adapter.in.kafka;

import kr.magicbox.order.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.order.adapter.in.kafka.event.DeliveryCompletedEvent;
import kr.magicbox.order.adapter.in.kafka.event.DeliveryStartedEvent;
import kr.magicbox.order.application.port.in.HandleDeliveryCompletedUseCase;
import kr.magicbox.order.application.port.in.HandleDeliveryStartedUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;
import kr.magicbox.order.global.exception.BusinessException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryEventKafkaListener {

    private final HandleDeliveryStartedUseCase handleDeliveryStartedUseCase;
    private final HandleDeliveryCompletedUseCase handleDeliveryCompletedUseCase;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.delivery-started", groupId = "order-service")
    public void handleDeliveryStarted(ConsumerRecord<String, DeliveryStartedEvent> consumerRecord) {
        log.info("[Inbox] delivery.started 이벤트 수신. eventId={}", consumerRecord.key());
        DeliveryStartedEvent event = consumerRecord.value();
        handleDeliveryStartedUseCase.handleDeliveryStarted(event.orderId(), event.orderLineId());
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.delivery-completed", groupId = "order-service")
    public void handleDeliveryCompleted(ConsumerRecord<String, DeliveryCompletedEvent> consumerRecord) {
        log.info("[Inbox] delivery.completed 이벤트 수신. eventId={}", consumerRecord.key());
        DeliveryCompletedEvent event = consumerRecord.value();
        handleDeliveryCompletedUseCase.handleDeliveryCompleted(event.orderId(), event.orderLineId());
    }
}
