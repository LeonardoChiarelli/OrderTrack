package br.com.OrderTrack.Order.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "processed_events")
@AllArgsConstructor
@NoArgsConstructor
public class ProcessedEventEntity {
    @Id
    private String messageId;
    private LocalDateTime processedAt;
}