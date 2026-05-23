package kr.magicbox.order.adapter.out.persistence.repository;

import kr.magicbox.order.adapter.out.persistence.entity.OrderEntity;
import kr.magicbox.order.domain.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT o FROM OrderEntity o WHERE o.id = :id AND o.isDeleted = false")
    Optional<OrderEntity> findByIdAndIsDeletedFalse(@Param("id") Long id);

    @Query("SELECT o FROM OrderEntity o WHERE o.customerId = :customerId AND o.isDeleted = false")
    List<OrderEntity> findByCustomerIdAndIsDeletedFalse(@Param("customerId") Long customerId);

    @Query("SELECT o FROM OrderEntity o WHERE o.sellerId = :sellerId AND o.isDeleted = false")
    List<OrderEntity> findBySellerIdAndIsDeletedFalse(@Param("sellerId") Long sellerId);

    @Query("SELECT o FROM OrderEntity o WHERE o.status = :status AND o.updatedAt < :before AND o.isDeleted = false")
    List<OrderEntity> findByStatusAndUpdatedAtBeforeAndIsDeletedFalse(@Param("status") OrderStatus status, @Param("before") Instant before, Pageable pageable);
}
