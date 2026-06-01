package kr.magicbox.order.adapter.out.persistence;

import kr.magicbox.order.adapter.out.persistence.entity.OrderEntity;
import kr.magicbox.order.adapter.out.persistence.entity.OrderLineEntity;
import kr.magicbox.order.adapter.out.persistence.mapper.OrderMapper;
import kr.magicbox.order.adapter.out.persistence.repository.OrderJpaRepository;
import kr.magicbox.order.adapter.out.persistence.repository.OrderLineJpaRepository;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.aggregate.OrderLine;
import kr.magicbox.order.domain.enums.OrderStatus;
import kr.magicbox.order.domain.exception.OrderNotFoundException;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderJpaAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderLineJpaRepository orderLineJpaRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderJpaRepository.save(orderMapper.toEntity(order));
        List<OrderLineEntity> savedLineEntities = order.getOrderLines().stream()
                .map(line -> orderLineJpaRepository.save(orderMapper.toLineEntity(orderEntity.getId(), line)))
                .toList();
        return orderMapper.toDomain(orderEntity, savedLineEntities);
    }

    @Override
    public void update(Order order) {
        OrderEntity entity = orderJpaRepository.findByIdAndIsDeletedFalse(order.getId().value())
                .orElseThrow(OrderNotFoundException::new);
        entity.update(order.getStatus());
        orderJpaRepository.save(entity);

        List<OrderLineEntity> lineEntities = orderLineJpaRepository.findByOrderId(entity.getId());
        Map<Long, OrderLineEntity> lineEntityById = lineEntities.stream()
                .collect(Collectors.toMap(OrderLineEntity::getId, l -> l));
        order.getOrderLines().forEach(line -> {
            if (line.getId() != null) {
                OrderLineEntity lineEntity = lineEntityById.get(line.getId().value());
                if (lineEntity != null) {
                    lineEntity.updateDeliveryStatus(line.getDeliveryStatus());
                    orderLineJpaRepository.save(lineEntity);
                }
            }
        });
    }

    @Override
    public Order findById(OrderId id) {
        OrderEntity entity = orderJpaRepository.findByIdAndIsDeletedFalse(id.value())
                .orElseThrow(OrderNotFoundException::new);
        List<OrderLineEntity> lineEntities = orderLineJpaRepository.findByOrderId(entity.getId());
        return orderMapper.toDomain(entity, lineEntities);
    }

    @Override
    public Order findByOrderLineId(Long orderLineId) {
        OrderLineEntity lineEntity = orderLineJpaRepository.findByOrderLineId(orderLineId)
                .orElseThrow(OrderNotFoundException::new);
        OrderEntity orderEntity = orderJpaRepository.findByIdAndIsDeletedFalse(lineEntity.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        List<OrderLineEntity> allLineEntities = orderLineJpaRepository.findByOrderId(orderEntity.getId());
        return orderMapper.toDomain(orderEntity, allLineEntities);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        List<OrderEntity> orders = orderJpaRepository.findByCustomerIdAndIsDeletedFalse(customerId);
        return toDomainsWithLines(orders);
    }

    @Override
    public List<Order> findBySellerId(Long sellerId) {
        List<OrderEntity> orders = orderJpaRepository.findBySellerIdAndIsDeletedFalse(sellerId);
        return toDomainsWithLines(orders);
    }

    @Override
    public List<Order> findDeliveredBefore(Instant deliveredBefore, int limit) {
        List<OrderEntity> orders = orderJpaRepository.findByStatusAndUpdatedAtBeforeAndIsDeletedFalse(
                OrderStatus.DELIVERED, deliveredBefore, PageRequest.of(0, limit));
        return toDomainsWithLines(orders);
    }

    private List<Order> toDomainsWithLines(List<OrderEntity> orders) {
        if (orders.isEmpty()) {
            return List.of();
        }
        List<Long> orderIds = orders.stream().map(OrderEntity::getId).toList();
        Map<Long, List<OrderLineEntity>> linesByOrderId = orderLineJpaRepository.findByOrderIdIn(orderIds)
                .stream()
                .collect(Collectors.groupingBy(OrderLineEntity::getOrderId));

        return orders.stream()
                .map(entity -> orderMapper.toDomain(entity, linesByOrderId.getOrDefault(entity.getId(), List.of())))
                .toList();
    }
}
