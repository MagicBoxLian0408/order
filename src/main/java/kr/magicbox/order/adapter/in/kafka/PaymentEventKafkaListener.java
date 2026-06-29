package kr.magicbox.order.adapter.in.kafka;

import kr.magicbox.order.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.order.adapter.in.kafka.event.PaymentCancelFailedEvent;
import kr.magicbox.order.adapter.in.kafka.event.PaymentCancelSucceededEvent;
import kr.magicbox.order.adapter.in.kafka.event.PaymentFailedEvent;
import kr.magicbox.order.adapter.in.kafka.event.PaymentSucceededEvent;
import kr.magicbox.order.adapter.out.persistence.entity.OrderInboxEntity;
import kr.magicbox.order.adapter.out.persistence.repository.OrderInboxJpaRepository;
import kr.magicbox.order.application.port.in.HandlePaymentCancelFailedUseCase;
import kr.magicbox.order.application.port.in.HandlePaymentCancelSucceededUseCase;
import kr.magicbox.order.application.port.in.HandlePaymentFailedUseCase;
import kr.magicbox.order.application.port.in.HandlePaymentSucceededUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;
import kr.magicbox.order.global.exception.BusinessException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaListener {

    private final HandlePaymentSucceededUseCase handlePaymentSucceededUseCase;
    private final HandlePaymentFailedUseCase handlePaymentFailedUseCase;
    private final HandlePaymentCancelSucceededUseCase handlePaymentCancelSucceededUseCase;
    private final HandlePaymentCancelFailedUseCase handlePaymentCancelFailedUseCase;
    private final OrderInboxJpaRepository orderInboxJpaRepository;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.payment-succeeded", groupId = "order-service")
    public void handlePaymentSucceeded(ConsumerRecord<String, PaymentSucceededEvent> consumerRecord) {
        log.info("[Inbox] payment.succeeded 이벤트 수신. eventId={}", consumerRecord.key());
        handlePaymentSucceededUseCase.handlePaymentSucceeded(consumerRecord.value().orderId());
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.payment-failed", groupId = "order-service")
    public void handlePaymentFailed(ConsumerRecord<String, PaymentFailedEvent> consumerRecord) {
        log.info("[Inbox] payment.failed 이벤트 수신. eventId={}", consumerRecord.key());
        handlePaymentFailedUseCase.handlePaymentFailed(consumerRecord.value().orderId());
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.payment-cancel-succeeded", groupId = "order-service")
    public void handlePaymentCancelSucceeded(ConsumerRecord<String, PaymentCancelSucceededEvent> consumerRecord) {
        log.info("[Inbox] payment.cancel.succeeded 이벤트 수신. eventId={}", consumerRecord.key());
        handlePaymentCancelSucceededUseCase.handlePaymentCancelSucceeded(consumerRecord.value().orderId());
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.payment-cancel-failed", groupId = "order-service")
    public void handlePaymentCancelFailed(ConsumerRecord<String, PaymentCancelFailedEvent> consumerRecord) {
        log.info("[Inbox] payment.cancel.failed 이벤트 수신. eventId={}", consumerRecord.key());
        handlePaymentCancelFailedUseCase.handlePaymentCancelFailed(consumerRecord.value().orderId());
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        orderInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(OrderInboxEntity::markDeadLettered);
    }
}
