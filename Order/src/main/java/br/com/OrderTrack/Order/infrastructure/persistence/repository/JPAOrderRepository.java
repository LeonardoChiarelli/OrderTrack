package br.com.OrderTrack.Order.infrastructure.persistence.repository;

import br.com.OrderTrack.Order.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JPAOrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findById(UUID id);
}
