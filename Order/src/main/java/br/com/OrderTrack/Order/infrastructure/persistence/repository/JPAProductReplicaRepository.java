package br.com.OrderTrack.Order.infrastructure.persistence.repository;

import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProductReplicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JPAProductReplicaRepository extends JpaRepository<ProductReplicaEntity, UUID> {
}
