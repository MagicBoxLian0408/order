package kr.magicbox.order.adapter.out.persistence.repository;

import kr.magicbox.order.adapter.out.persistence.entity.OrderOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutboxEntity, Long> {
}
