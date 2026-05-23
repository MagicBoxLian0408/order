package kr.magicbox.order.adapter.out.persistence.mapper;

import kr.magicbox.order.adapter.out.persistence.entity.OrderEntity;
import kr.magicbox.order.adapter.out.persistence.entity.OrderLineEntity;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.aggregate.OrderLine;
import kr.magicbox.order.domain.vo.OrderId;
import kr.magicbox.order.domain.vo.OrderLineId;
import kr.magicbox.order.domain.vo.ShippingAddress;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderEntity toEntity(Order domain) {
        ShippingAddress addr = domain.getShippingAddress();
        return OrderEntity.builder()
                .customerId(domain.getCustomerId())
                .sellerId(domain.getSellerId())
                .status(domain.getStatus())
                .totalAmount(domain.getTotalAmount())
                .recipient(addr.recipient())
                .phone(addr.phone())
                .zipCode(addr.zipCode())
                .address1(addr.address1())
                .address2(addr.address2())
                .build();
    }

    public OrderLineEntity toLineEntity(Long orderId, OrderLine domain) {
        return OrderLineEntity.builder()
                .orderId(orderId)
                .productId(domain.getProductId())
                .sellerId(domain.getSellerId())
                .productName(domain.getProductName())
                .quantity(domain.getQuantity())
                .unitPrice(domain.getUnitPrice())
                .deliveryStatus(domain.getDeliveryStatus())
                .build();
    }

    public Order toDomain(OrderEntity entity, List<OrderLineEntity> lineEntities) {
        List<OrderLine> orderLines = lineEntities.stream()
                .map(this::toLineDomain)
                .toList();

        return Order.reconstructBuilder()
                .id(OrderId.of(entity.getId()))
                .customerId(entity.getCustomerId())
                .sellerId(entity.getSellerId())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .shippingAddress(ShippingAddress.of(
                        entity.getRecipient(),
                        entity.getPhone(),
                        entity.getZipCode(),
                        entity.getAddress1(),
                        entity.getAddress2()
                ))
                .orderLines(orderLines)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private OrderLine toLineDomain(OrderLineEntity entity) {
        return OrderLine.reconstructBuilder()
                .id(OrderLineId.of(entity.getId()))
                .productId(entity.getProductId())
                .sellerId(entity.getSellerId())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .deliveryStatus(entity.getDeliveryStatus())
                .build();
    }
}
