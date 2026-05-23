package kr.magicbox.order.domain.aggregate;

import kr.magicbox.order.domain.enums.OrderLineDeliveryStatus;
import kr.magicbox.order.domain.enums.OrderStatus;
import kr.magicbox.order.domain.exception.InvalidFieldException;
import kr.magicbox.order.domain.exception.OrderLineComplainNotAllowedException;
import kr.magicbox.order.domain.exception.OrderStatusConflictException;
import kr.magicbox.order.domain.exception.OrderLineNotFoundException;
import kr.magicbox.order.domain.vo.OrderId;
import kr.magicbox.order.domain.vo.ShippingAddress;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Order {

    private final OrderId id;
    private final Long customerId;
    private final Long sellerId;
    private OrderStatus status;
    private final Long totalAmount;
    private final ShippingAddress shippingAddress;
    private final List<OrderLine> orderLines;
    private final Instant createdAt;
    private Instant updatedAt;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public Order(Long customerId, Long sellerId, Long totalAmount,
                 ShippingAddress shippingAddress, List<OrderLine> orderLines) {
        validateCreate(customerId, sellerId, totalAmount, shippingAddress);
        this.id = null;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.status = OrderStatus.PENDING;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.orderLines = orderLines != null ? new ArrayList<>(orderLines) : new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public Order(OrderId id, Long customerId, Long sellerId, OrderStatus status,
                 Long totalAmount, ShippingAddress shippingAddress,
                 List<OrderLine> orderLines, Instant createdAt, Instant updatedAt) {
        validateReconstruct(id, customerId, sellerId, status, totalAmount, shippingAddress, createdAt, updatedAt);
        this.id = id;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.orderLines = orderLines != null ? new ArrayList<>(orderLines) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private void validateCreate(Long customerId, Long sellerId, Long totalAmount, ShippingAddress shippingAddress) {
        if (customerId == null || customerId <= 0) throw new InvalidFieldException("구매자 ID는 양수여야 합니다.");
        if (sellerId == null || sellerId <= 0) throw new InvalidFieldException("판매자 ID는 양수여야 합니다.");
        if (totalAmount == null || totalAmount < 0) throw new InvalidFieldException("총 금액은 0 이상이어야 합니다.");
        if (shippingAddress == null) throw new InvalidFieldException("배송지 정보는 필수 값입니다.");
    }

    private void validateReconstruct(OrderId id, Long customerId, Long sellerId, OrderStatus status,
                                     Long totalAmount, ShippingAddress shippingAddress,
                                     Instant createdAt, Instant updatedAt) {
        if (id == null) throw new InvalidFieldException("주문 ID는 필수입니다.");
        if (customerId == null || customerId <= 0) throw new InvalidFieldException("구매자 ID는 양수여야 합니다.");
        if (sellerId == null || sellerId <= 0) throw new InvalidFieldException("판매자 ID는 양수여야 합니다.");
        if (status == null) throw new InvalidFieldException("주문 상태는 필수입니다.");
        if (totalAmount == null || totalAmount < 0) throw new InvalidFieldException("총 금액은 0 이상이어야 합니다.");
        if (shippingAddress == null) throw new InvalidFieldException("배송지 정보는 필수입니다.");
        if (createdAt == null) throw new InvalidFieldException("생성 시각은 필수입니다.");
        if (updatedAt == null) throw new InvalidFieldException("수정 시각은 필수입니다.");
    }

    public void reserveStock() {
        validateStatus(OrderStatus.PENDING);
        this.status = OrderStatus.STOCK_RESERVED;
        this.updatedAt = Instant.now();
    }

    public void completePayment() {
        validateStatus(OrderStatus.STOCK_RESERVED);
        this.status = OrderStatus.PAYMENT_COMPLETED;
        this.updatedAt = Instant.now();
    }

    /**
     * 모든 OrderLine을 PREPARING 상태로 전환한다.
     * Order도 PREPARING으로 전환한다.
     */
    public void prepare() {
        validateStatus(OrderStatus.PAYMENT_COMPLETED);
        orderLines.forEach(OrderLine::prepare);
        this.status = OrderStatus.PREPARING;
        this.updatedAt = Instant.now();
    }

    /**
     * 특정 OrderLine을 CONFIRMED 상태로 전환한다.
     * 모든 라인이 CONFIRMED 이상이면 Order도 CONFIRMED로 전환한다.
     */
    public void confirmOrderLine(Long orderLineId) {
        validateStatus(OrderStatus.PREPARING);
        OrderLine orderLine = findOrderLine(orderLineId);
        orderLine.confirm();
        this.updatedAt = Instant.now();

        if (isAllLinesAtLeast()) {
            this.status = OrderStatus.CONFIRMED;
        }
    }

    /**
     * 모든 OrderLine을 CONFIRMED 상태로 전환한다 (판매자 전체 확정).
     * Order도 CONFIRMED로 전환한다.
     */
    public void confirm() {
        if (this.status != OrderStatus.PREPARING) {
            throw new OrderStatusConflictException("현재 상태에서 확정 처리할 수 없습니다. 현재: " + this.status);
        }
        orderLines.forEach(OrderLine::confirm);
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = Instant.now();
    }

    /**
     * 특정 OrderLine의 배송을 시작한다.
     * 첫 번째 라인 배송 시작 시 Order를 DELIVERING으로 전환한다.
     */
    public void startDelivery(Long orderLineId) {
        if (this.status != OrderStatus.CONFIRMED && this.status != OrderStatus.DELIVERING) {
            throw new OrderStatusConflictException("현재 상태에서 배송을 시작할 수 없습니다. 현재: " + this.status);
        }
        OrderLine orderLine = findOrderLine(orderLineId);
        orderLine.startDelivery();
        this.status = OrderStatus.DELIVERING;
        this.updatedAt = Instant.now();
    }

    /**
     * 특정 OrderLine의 배송을 완료한다.
     * 모든 라인 SHIPPED → DELIVERED, 하나라도 COMPLAINING 포함 → COMPLAINING
     */
    public void completeDelivery(Long orderLineId) {
        validateStatus(OrderStatus.DELIVERING);
        OrderLine orderLine = findOrderLine(orderLineId);
        orderLine.completeDelivery();
        this.updatedAt = Instant.now();
        updateDeliveryStatus();
    }

    /**
     * 배달 중(SHIPPING) 상태의 OrderLine에 대해 미수령 신고를 처리한다.
     * 모든 라인 완료 시 하나라도 COMPLAINING이면 Order → COMPLAINING, 전부 SHIPPED면 → DELIVERED
     */
    public void complainOrderLine(Long orderLineId) {
        if (this.status != OrderStatus.DELIVERING) {
            throw new OrderLineComplainNotAllowedException();
        }
        OrderLine orderLine = findOrderLine(orderLineId);
        orderLine.complain();
        this.updatedAt = Instant.now();
        updateDeliveryStatus();
    }

    public boolean isAllDelivered() {
        return this.status == OrderStatus.DELIVERED || this.status == OrderStatus.COMPLAINING;
    }

    private void updateDeliveryStatus() {
        boolean allDone = orderLines.stream()
                .allMatch(line -> line.getDeliveryStatus() == OrderLineDeliveryStatus.SHIPPED
                        || line.getDeliveryStatus() == OrderLineDeliveryStatus.COMPLAINING);
        if (!allDone) return;

        boolean hasComplain = orderLines.stream()
                .anyMatch(line -> line.getDeliveryStatus() == OrderLineDeliveryStatus.COMPLAINING);
        this.status = hasComplain ? OrderStatus.COMPLAINING : OrderStatus.DELIVERED;
    }

    public List<OrderLine> shippedLines() {
        return orderLines.stream()
                .filter(line -> line.getDeliveryStatus() == OrderLineDeliveryStatus.SHIPPED)
                .toList();
    }

    public void confirmPurchase() {
        validateStatus(OrderStatus.DELIVERED);
        this.status = OrderStatus.PURCHASE_CONFIRMED;
        this.updatedAt = Instant.now();
    }

    /**
     * 특정 OrderLine에 대해 취소를 요청한다.
     * 모든 라인이 CANCEL_REQUESTED 상태가 되면 Order를 CANCELLING으로 전환한다.
     */
    public void cancelOrderLine(Long orderLineId) {
        if (this.status == OrderStatus.CANCELLED || this.status == OrderStatus.PURCHASE_CONFIRMED) {
            throw new OrderStatusConflictException("취소할 수 없는 주문 상태입니다: " + this.status);
        }
        OrderLine orderLine = findOrderLine(orderLineId);
        orderLine.requestCancel();
        this.updatedAt = Instant.now();

        if (orderLines.stream().allMatch(OrderLine::isCancelRequested)) {
            this.status = OrderStatus.CANCELLING;
        }
    }

    public void completeCancellation() {
        validateStatus(OrderStatus.CANCELLING);
        orderLines.forEach(OrderLine::completeCancellation);
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    public void failCancellation() {
        validateStatus(OrderStatus.CANCELLING);
        this.status = OrderStatus.CANCELLATION_FAILED;
        this.updatedAt = Instant.now();
    }

    public void failStock() {
        validateStatus(OrderStatus.PENDING);
        this.status = OrderStatus.STOCK_FAILED;
        this.updatedAt = Instant.now();
    }

    public void failPayment() {
        validateStatus(OrderStatus.STOCK_RESERVED);
        this.status = OrderStatus.PAYMENT_FAILED;
        this.updatedAt = Instant.now();
    }

    private boolean isAllLinesAtLeast() {
        return orderLines.stream().allMatch(line -> line.isAtLeast(OrderLineDeliveryStatus.CONFIRMED));
    }

    private OrderLine findOrderLine(Long orderLineId) {
        return orderLines.stream()
                .filter(line -> line.getId() != null && line.getId().value().equals(orderLineId))
                .findFirst()
                .orElseThrow(OrderLineNotFoundException::new);
    }

    private void validateStatus(OrderStatus expected) {
        if (this.status != expected) {
            throw new OrderStatusConflictException(
                    "현재 상태에서 해당 작업을 수행할 수 없습니다. 현재: " + this.status + ", 기대: " + expected);
        }
    }
}
