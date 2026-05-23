package kr.magicbox.order.adapter.out.persistence.repository;

import kr.magicbox.order.adapter.out.persistence.entity.OrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderLineJpaRepository extends JpaRepository<OrderLineEntity, Long> {

    @Query("SELECT ol FROM OrderLineEntity ol WHERE ol.orderId = :orderId")
    List<OrderLineEntity> findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT ol FROM OrderLineEntity ol WHERE ol.orderId IN :orderIds")
    List<OrderLineEntity> findByOrderIdIn(@Param("orderIds") List<Long> orderIds);

    @Query("SELECT ol FROM OrderLineEntity ol WHERE ol.id = :orderLineId")
    Optional<OrderLineEntity> findByOrderLineId(@Param("orderLineId") Long orderLineId);
}
