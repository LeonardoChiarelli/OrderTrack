package br.com.OrderTrack.Order.infrastructure.persistence.repository;

import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JPAProcessedEventsRepository extends JpaRepository<ProcessedEventEntity, String> {
}
