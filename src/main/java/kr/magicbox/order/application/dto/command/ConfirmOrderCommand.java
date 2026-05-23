package kr.magicbox.order.application.dto.command;

import lombok.Builder;

@Builder
public record ConfirmOrderCommand(Long orderId, Long sellerId) {}
