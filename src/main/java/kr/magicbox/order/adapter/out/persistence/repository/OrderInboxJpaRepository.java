package kr.magicbox.order.adapter.out.persistence.repository;

import kr.magicbox.order.adapter.out.persistence.entity.OrderInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderInboxJpaRepository extends JpaRepository<OrderInboxEntity, Long> {

    @Query("SELECT CASE WHEN EXISTS (SELECT i FROM OrderInboxEntity i WHERE i.eventId = :eventId) THEN true ELSE false END")
    boolean existsByEventId(@Param("eventId") Long eventId);
}
