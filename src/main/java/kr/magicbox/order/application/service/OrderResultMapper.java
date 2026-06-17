package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.result.OrderLineResult;
import kr.magicbox.order.application.dto.result.OrderResult;
import kr.magicbox.order.application.dto.result.ShippingAddressResult;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.aggregate.OrderLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderResultMapper {

    public OrderResult toResult(Order order) {
        return toResult(order, null);
    }

    public OrderResult toResult(Order order, Long sellerId) {
        List<OrderLine> lines = order.getOrderLines();
        if (sellerId != null) {
            lines = lines.stream()
                    .filter(line -> sellerId.equals(line.getSellerId()))
                    .toList();
        }

        List<OrderLineResult> lineResults = lines.stream()
                .map(line -> OrderLineResult.builder()
                        .orderLineId(line.getId() != null ? line.getId().value() : null)
                        .productId(line.getProductId())
                        .productName(line.getProductName())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .productType(line.getProductType())
                        .thumbnailUrl(line.getThumbnailUrl())
                        .deliveryStatus(line.getDeliveryStatus())
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
