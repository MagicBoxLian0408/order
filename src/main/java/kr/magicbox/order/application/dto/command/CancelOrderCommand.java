package kr.magicbox.order.application.dto.command;

import lombok.Builder;

@Builder
public record CancelOrderCommand(Long orderId, Long orderLineId, Long customerId, String reason) {}
