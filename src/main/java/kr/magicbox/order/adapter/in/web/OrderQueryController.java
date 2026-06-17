package kr.magicbox.order.adapter.in.web;

import kr.magicbox.order.adapter.in.web.dto.response.OrderResponse;
import kr.magicbox.order.application.dto.query.GetOrderListQuery;
import kr.magicbox.order.application.dto.query.GetOrderQuery;
import kr.magicbox.order.application.dto.result.OrderResult;
import kr.magicbox.order.application.port.in.GetOrderListUseCase;
import kr.magicbox.order.application.port.in.GetOrderUseCase;
import kr.magicbox.order.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderQueryController {

    private final GetOrderUseCase getOrderUseCase;
    private final GetOrderListUseCase getOrderListUseCase;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long orderId
    ) {
        OrderResult result = getOrderUseCase.getOrder(GetOrderQuery.builder()
                .orderId(orderId)
                .requesterId(userId.value())
                .build());
        return ResponseEntity.ok(OrderResponse.from(result));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrderList(
            @RequestParam(value = "customer_id", required = false) Long customerId,
            @RequestParam(value = "seller_id", required = false) Long sellerId
    ) {
        List<OrderResult> results = getOrderListUseCase.getOrderList(GetOrderListQuery.builder()
                .customerId(customerId)
                .sellerId(sellerId)
                .build());
        return ResponseEntity.ok(results.stream().map(OrderResponse::from).toList());
    }
}
