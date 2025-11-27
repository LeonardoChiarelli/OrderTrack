package br.com.OrderTrack.Payment.domain.repository;

import br.com.OrderTrack.Payment.domain.model.Payment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p WHERE p.status = 'PROCESSING' AND p.order.id = :orderId")
    boolean existsByStatusAndOrder(@NotNull Long orderId);

    @Modifying
    @Query("DELETE FROM Payment p WHERE p.status = 'CANCELLED'")
    void deleteAllByStatusCancelled();
}
