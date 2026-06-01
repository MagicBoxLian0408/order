package kr.magicbox.order.adapter.in.kafka;

import tools.jackson.databind.ObjectMapper;
import kr.magicbox.order.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.order.adapter.in.kafka.event.OrderPrepareEventDto;
import kr.magicbox.order.application.port.in.HandleOrderPrepareUseCase;
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
public class OrderStateKafkaListener {

    private final HandleOrderPrepareUseCase handleOrderPrepareUseCase;
    private final ObjectMapper objectMapper;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(
            topics = "outbox.event.order-prepare",
            groupId = "order-service",
            containerFactory = "debeziumKafkaListenerContainerFactory"
    )
    public void handleOrderPrepare(ConsumerRecord<String, String> consumerRecord) {
        log.info("[Inbox] order.prepare 이벤트 수신. key={}", consumerRecord.key());
        OrderPrepareEventDto event = objectMapper.readValue(consumerRecord.value(), OrderPrepareEventDto.class);
        handleOrderPrepareUseCase.handleOrderPrepare(event.orderId());
    }
}
