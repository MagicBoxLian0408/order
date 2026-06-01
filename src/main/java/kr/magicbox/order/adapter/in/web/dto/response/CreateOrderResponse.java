package kr.magicbox.order.adapter.in.web.dto.response;

import kr.magicbox.order.application.dto.result.CreateOrderResult;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateOrderResponse(
        Long orderId,
        Long sellerId,
        Long totalAmount,
        List<OrderLineResponse> orderLines
) {
    public static CreateOrderResponse from(CreateOrderResult result) {
        List<OrderLineResponse> orderLineResponses = result.orderLines().stream()
                .map(OrderLineResponse::from)
                .toList();

        return CreateOrderResponse.builder()
                .orderId(result.orderId())
                .sellerId(result.sellerId())
                .totalAmount(result.totalAmount())
                .orderLines(orderLineResponses)
                .build();
    }
}
