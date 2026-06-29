package kr.magicbox.order.adapter.out.persistence.repository;

import kr.magicbox.order.adapter.out.persistence.entity.OrderInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderInboxJpaRepository extends JpaRepository<OrderInboxEntity, Long> {

    boolean existsByEventKey(String eventKey);

    Optional<OrderInboxEntity> findByTopicAndPartitionAndOffset(String topic, Integer partition, Long offset);
}
