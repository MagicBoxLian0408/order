package kr.magicbox.order.application.dto.command;

import lombok.Builder;

@Builder
public record PurchaseConfirmOrderCommand(Long orderId, Long customerId) {}
