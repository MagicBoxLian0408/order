package kr.magicbox.order.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kr.magicbox.order.domain.enums.OrderLineDeliveryStatus;
import kr.magicbox.order.domain.enums.ProductType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_line")
public class OrderLineEntity extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Long unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private OrderLineDeliveryStatus deliveryStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Builder
    public OrderLineEntity(Long orderId, Long productId, Long sellerId, String productName, Integer quantity, Long unitPrice, OrderLineDeliveryStatus deliveryStatus, ProductType productType) {
        this.orderId = orderId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.deliveryStatus = deliveryStatus != null ? deliveryStatus : OrderLineDeliveryStatus.PENDING;
        this.productType = productType;
    }

    public void updateDeliveryStatus(OrderLineDeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
