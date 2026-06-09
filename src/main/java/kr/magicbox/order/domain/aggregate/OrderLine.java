package kr.magicbox.order.domain.aggregate;

import kr.magicbox.order.domain.enums.OrderLineDeliveryStatus;
import kr.magicbox.order.domain.enums.ProductType;
import kr.magicbox.order.domain.exception.InvalidFieldException;
import kr.magicbox.order.domain.exception.OrderLineComplainNotAllowedException;
import kr.magicbox.order.domain.exception.OrderStatusConflictException;
import kr.magicbox.order.domain.vo.OrderLineId;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderLine {

    private final OrderLineId id;
    private final Long productId;
    private final Long sellerId;
    private final String productName;
    private final Integer quantity;
    private final Long unitPrice;
    private final ProductType productType;
    private final String thumbnailUrl;
    private OrderLineDeliveryStatus deliveryStatus;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public OrderLine(Long productId, Long sellerId, String productName, Integer quantity, Long unitPrice, ProductType productType, String thumbnailUrl) {
        validateCreate(productId, quantity, unitPrice);
        this.id = null;
        this.productId = productId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productType = productType;
        this.thumbnailUrl = thumbnailUrl;
        this.deliveryStatus = OrderLineDeliveryStatus.PENDING;
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public OrderLine(OrderLineId id, Long productId, Long sellerId, String productName,
                     Integer quantity, Long unitPrice, ProductType productType, String thumbnailUrl, OrderLineDeliveryStatus deliveryStatus) {
        validateReconstruct(id, productId, sellerId, productName, quantity, unitPrice, deliveryStatus);
        this.id = id;
        this.productId = productId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productType = productType;
        this.thumbnailUrl = thumbnailUrl;
        this.deliveryStatus = deliveryStatus;
    }

    private void validateCreate(Long productId, Integer quantity, Long unitPrice) {
        if (productId == null || productId <= 0) throw new InvalidFieldException("상품 ID는 양수여야 합니다.");
        if (quantity == null || quantity <= 0) throw new InvalidFieldException("주문 수량은 1 이상이어야 합니다.");
        if (unitPrice == null || unitPrice < 0) throw new InvalidFieldException("단가는 0 이상이어야 합니다.");
    }

    private void validateReconstruct(OrderLineId id, Long productId, Long sellerId, String productName,
                                     Integer quantity, Long unitPrice, OrderLineDeliveryStatus deliveryStatus) {
        if (id == null) throw new InvalidFieldException("주문 라인 ID는 필수입니다.");
        if (productId == null || productId <= 0) throw new InvalidFieldException("상품 ID는 양수여야 합니다.");
        if (sellerId == null || sellerId <= 0) throw new InvalidFieldException("판매자 ID는 양수여야 합니다.");
        if (productName == null || productName.isBlank()) throw new InvalidFieldException("상품명은 필수입니다.");
        if (quantity == null || quantity <= 0) throw new InvalidFieldException("주문 수량은 1 이상이어야 합니다.");
        if (unitPrice == null || unitPrice < 0) throw new InvalidFieldException("단가는 0 이상이어야 합니다.");
        if (deliveryStatus == null) throw new InvalidFieldException("배송 상태는 필수입니다.");
    }

    public long lineTotal() {
        return unitPrice * quantity;
    }

    public void prepare() {
        if (this.deliveryStatus != OrderLineDeliveryStatus.PENDING) {
            throw new OrderStatusConflictException("준비 처리할 수 없는 상태입니다: " + this.deliveryStatus);
        }
        this.deliveryStatus = OrderLineDeliveryStatus.PREPARING;
    }

    public void confirm() {
        if (this.deliveryStatus != OrderLineDeliveryStatus.PREPARING) {
            throw new OrderStatusConflictException("확정 처리할 수 없는 상태입니다: " + this.deliveryStatus);
        }
        this.deliveryStatus = OrderLineDeliveryStatus.CONFIRMED;
    }

    public void startDelivery() {
        if (this.deliveryStatus != OrderLineDeliveryStatus.CONFIRMED) {
            throw new OrderStatusConflictException("배송 시작할 수 없는 상태입니다: " + this.deliveryStatus);
        }
        this.deliveryStatus = OrderLineDeliveryStatus.SHIPPING;
    }

    public void completeDelivery() {
        if (this.deliveryStatus != OrderLineDeliveryStatus.SHIPPING) {
            throw new OrderStatusConflictException("배송 완료 처리할 수 없는 상태입니다: " + this.deliveryStatus);
        }
        this.deliveryStatus = OrderLineDeliveryStatus.SHIPPED;
    }

    public void complain() {
        if (this.deliveryStatus != OrderLineDeliveryStatus.SHIPPING) {
            throw new OrderLineComplainNotAllowedException();
        }
        this.deliveryStatus = OrderLineDeliveryStatus.COMPLAINING;
    }

    public void requestCancel() {
        if (this.deliveryStatus == OrderLineDeliveryStatus.SHIPPED
                || this.deliveryStatus == OrderLineDeliveryStatus.COMPLAINING
                || this.deliveryStatus == OrderLineDeliveryStatus.CANCEL_REQUESTED
                || this.deliveryStatus == OrderLineDeliveryStatus.CANCELLED) {
            throw new OrderStatusConflictException("취소 요청할 수 없는 상태입니다: " + this.deliveryStatus);
        }
        this.deliveryStatus = OrderLineDeliveryStatus.CANCEL_REQUESTED;
    }

    public void completeCancellation() {
        if (this.deliveryStatus != OrderLineDeliveryStatus.CANCEL_REQUESTED) {
            throw new OrderStatusConflictException("취소 완료 처리할 수 없는 상태입니다: " + this.deliveryStatus);
        }
        this.deliveryStatus = OrderLineDeliveryStatus.CANCELLED;
    }

    public boolean isCancelRequested() {
        return this.deliveryStatus == OrderLineDeliveryStatus.CANCEL_REQUESTED;
    }

    public boolean isAtLeast(OrderLineDeliveryStatus target) {
        return this.deliveryStatus.ordinal() >= target.ordinal();
    }
}
