package kr.magicbox.order.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.magicbox.order.application.dto.command.CancelOrderCommand;

public record CancelOrderRequest(
        @NotBlank(message = "취소 사유는 필수입니다.") String reason
) {
    public CancelOrderCommand toCommand(Long orderId, Long orderLineId, Long customerId) {
        return CancelOrderCommand.builder()
                .orderId(orderId)
                .orderLineId(orderLineId)
                .customerId(customerId)
                .reason(reason)
                .build();
    }
}
