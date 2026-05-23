package kr.magicbox.order.domain.enums;

public enum OrderLineDeliveryStatus {
    PENDING,
    PREPARING,
    CONFIRMED,
    SHIPPING,
    SHIPPED,
    COMPLAINING,
    CANCEL_REQUESTED,
    CANCELLED
}
