package kr.magicbox.order.adapter.in.kafka;

import kr.magicbox.order.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.order.adapter.in.kafka.event.StockReserveFailedEvent;
import kr.magicbox.order.adapter.in.kafka.event.StockReserveSucceededEvent;
import kr.magicbox.order.application.port.in.HandleStockReserveFailedUseCase;
import kr.magicbox.order.application.port.in.HandleStockReserveSucceededUseCase;
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
public class StockEventKafkaListener {

    private final HandleStockReserveSucceededUseCase handleStockReserveSucceededUseCase;
    private final HandleStockReserveFailedUseCase handleStockReserveFailedUseCase;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.stock-reserve-succeeded", groupId = "order-service")
    public void handleStockReserveSucceeded(ConsumerRecord<String, StockReserveSucceededEvent> consumerRecord) {
        log.info("[Inbox] stock.reserve.succeeded 이벤트 수신. eventId={}", consumerRecord.key());
        handleStockReserveSucceededUseCase.handleStockReserveSucceeded(consumerRecord.value().orderId());
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.stock-reserve-failed", groupId = "order-service")
    public void handleStockReserveFailed(ConsumerRecord<String, StockReserveFailedEvent> consumerRecord) {
        log.info("[Inbox] stock.reserve.failed 이벤트 수신. eventId={}", consumerRecord.key());
        handleStockReserveFailedUseCase.handleStockReserveFailed(consumerRecord.value().orderId());
    }
}
