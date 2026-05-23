package kr.magicbox.order.application.dto.query;

import lombok.Builder;

@Builder
public record GetOrderListQuery(Long customerId, Long sellerId) {}
