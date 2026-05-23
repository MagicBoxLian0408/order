package kr.magicbox.order.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderDomainEventType {
    ORDER_PREPARE("order-prepare"),
    ORDER_CONFIRMED("order-confirmed"),
    ORDER_CANCEL("order-cancel"),
    ORDER_PURCHASE_CONFIRMED("order-purchase-confirmed"),
    ORDER_AUTO_CONFIRMED("order-auto-confirmed"),
    ORDER_DELIVERED("order-delivered"),
    RELEASE_SOLD_QUANTITY_INCREASE("release-sold-quantity-increase");

    private final String value;
}
