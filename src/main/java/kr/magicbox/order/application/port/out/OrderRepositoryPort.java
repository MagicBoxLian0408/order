package kr.magicbox.order.application.port.out;

import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.vo.OrderId;

import java.time.Instant;
import java.util.List;

public interface OrderRepositoryPort {
    Long save(Order order);
    void update(Order order);
    Order findById(OrderId id);
    Order findByOrderLineId(Long orderLineId);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findBySellerId(Long sellerId);
    List<Order> findDeliveredBefore(Instant deliveredBefore, int limit);
}
