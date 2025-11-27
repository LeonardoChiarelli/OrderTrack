package br.com.OrderTrack.Payment.domain.repository;

import br.com.OrderTrack.Payment.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order, Long> {
}
