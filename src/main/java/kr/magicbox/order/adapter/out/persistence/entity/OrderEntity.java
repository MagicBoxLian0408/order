package kr.magicbox.order.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.order.domain.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "address1", nullable = false)
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Version
    private Integer version;

    @Builder
    public OrderEntity(Long customerId, Long sellerId, OrderStatus status, Long totalAmount,
                       String recipient, String phone, String zipCode, String address1, String address2) {
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.recipient = recipient;
        this.phone = phone;
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
        this.isDeleted = false;
    }

    public void update(OrderStatus status) {
        this.status = status;
    }
}
