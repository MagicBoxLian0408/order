package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.result.OrderLineResult;
import kr.magicbox.order.application.dto.result.OrderResult;
import kr.magicbox.order.application.dto.result.ShippingAddressResult;
import kr.magicbox.order.domain.aggregate.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderResultMapper {

    public OrderResult toResult(Order order) {
        List<OrderLineResult> lineResults = order.getOrderLines().stream()
                .map(line -> OrderLineResult.builder()
                        .orderLineId(line.getId() != null ? line.getId().value() : null)
                        .productId(line.getProductId())
                        .productName(line.getProductName())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .build())
                .toList();

        return OrderResult.builder()
                .orderId(order.getId().value())
                .customerId(order.getCustomerId())
                .sellerId(order.getSellerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(ShippingAddressResult.from(order.getShippingAddress()))
                .orderLines(lineResults)
                .createdAt(order.getCreatedAt())
                .build();
    }
}
