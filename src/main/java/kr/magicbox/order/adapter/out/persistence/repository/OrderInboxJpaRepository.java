package kr.magicbox.order.adapter.out.persistence.repository;

import kr.magicbox.order.adapter.out.persistence.entity.OrderInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInboxJpaRepository extends JpaRepository<OrderInboxEntity, Long> {

    boolean existsByEventKey(String eventKey);
}
