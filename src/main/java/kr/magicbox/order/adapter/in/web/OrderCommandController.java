package kr.magicbox.order.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.order.adapter.in.web.dto.request.CancelOrderRequest;
import kr.magicbox.order.adapter.in.web.dto.request.CreateOrderRequest;
import kr.magicbox.order.adapter.in.web.dto.request.CreateReleaseOrderRequest;
import kr.magicbox.order.adapter.in.web.dto.response.CreateOrderResponse;
import kr.magicbox.order.application.dto.command.ConfirmOrderCommand;
import kr.magicbox.order.application.dto.command.PurchaseConfirmOrderCommand;
import kr.magicbox.order.application.port.in.CancelOrderUseCase;
import kr.magicbox.order.application.port.in.ComplainOrderLineUseCase;
import kr.magicbox.order.application.port.in.ConfirmOrderUseCase;
import kr.magicbox.order.application.port.in.CreateOrderUseCase;
import kr.magicbox.order.application.port.in.CreateReleaseOrderUseCase;
import kr.magicbox.order.application.port.in.PurchaseConfirmOrderUseCase;
import kr.magicbox.order.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class OrderCommandController {

    private final CreateOrderUseCase createOrderUseCase;
    private final CreateReleaseOrderUseCase createReleaseOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final PurchaseConfirmOrderUseCase purchaseConfirmOrderUseCase;
    private final ComplainOrderLineUseCase complainOrderLineUseCase;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @AuthenticationPrincipal UserId userId,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        CreateOrderResponse response = CreateOrderResponse.from(
                createOrderUseCase.createOrder(request.toCommand(userId.value())));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/release")
    public ResponseEntity<CreateOrderResponse> createReleaseOrder(
            @AuthenticationPrincipal UserId userId,
            @Valid @RequestBody CreateReleaseOrderRequest request
    ) {
        CreateOrderResponse response = CreateOrderResponse.from(
                createReleaseOrderUseCase.createReleaseOrder(request.toCommand(userId.value())));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long orderId
    ) {
        confirmOrderUseCase.confirmOrder(toConfirmCommand(orderId, userId.value()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/lines/{orderLineId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long orderId,
            @PathVariable Long orderLineId,
            @Valid @RequestBody CancelOrderRequest request
    ) {
        cancelOrderUseCase.cancelOrder(request.toCommand(orderId, orderLineId, userId.value()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/purchase-confirm")
    public ResponseEntity<Void> purchaseConfirmOrder(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long orderId
    ) {
        purchaseConfirmOrderUseCase.purchaseConfirmOrder(toPurchaseConfirmCommand(orderId, userId.value()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/lines/{orderLineId}/complain")
    public ResponseEntity<Void> complainOrderLine(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long orderId,
            @PathVariable Long orderLineId
    ) {
        complainOrderLineUseCase.complainOrderLine(orderId, orderLineId, userId.value());
        return ResponseEntity.noContent().build();
    }

    private ConfirmOrderCommand toConfirmCommand(Long orderId, Long sellerId) {
        return ConfirmOrderCommand.builder()
                .orderId(orderId)
                .sellerId(sellerId)
                .build();
    }

    private PurchaseConfirmOrderCommand toPurchaseConfirmCommand(Long orderId, Long customerId) {
        return PurchaseConfirmOrderCommand.builder()
                .orderId(orderId)
                .customerId(customerId)
                .build();
    }
}
