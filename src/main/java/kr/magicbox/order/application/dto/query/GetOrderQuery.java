package kr.magicbox.order.application.dto.query;

import lombok.Builder;

@Builder
public record GetOrderQuery(Long orderId, Long requesterId) {}
