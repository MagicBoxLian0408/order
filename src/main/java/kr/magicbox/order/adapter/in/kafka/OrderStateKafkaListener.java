package kr.magicbox.order.adapter.in.kafka;

import kr.magicbox.order.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.order.adapter.in.kafka.event.OrderPrepareConfirmedEvent;
import kr.magicbox.order.application.port.in.HandleOrderPrepareUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStateKafkaListener {

    private final HandleOrderPrepareUseCase handleOrderPrepareUseCase;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {kr.magicbox.order.global.exception.BusinessException.class})
    @KafkaListener(topics = "outbox.event.order-prepare-confirmed", groupId = "order-service")
    public void handleOrderPrepareConfirmed(ConsumerRecord<String, OrderPrepareConfirmedEvent> consumerRecord) {
        log.info("[Inbox] order.prepare.confirmed 이벤트 수신. orderId={}", consumerRecord.value().orderId());
        handleOrderPrepareUseCase.handleOrderPrepare(consumerRecord.value().orderId());
    }

}
