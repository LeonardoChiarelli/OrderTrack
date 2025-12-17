package br.com.OrderTrack.Order.infrastructure.messaging.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {
    List<OutboxEntity> findByProcessedFalse(Pageable pageable);

    @Modifying
    @Query("DELETE FROM OutboxEntity o WHERE o.processed = true AND o.createdAt < :cutoff")
    void deleteProcessedOlderThan(LocalDateTime cutoff);
}
