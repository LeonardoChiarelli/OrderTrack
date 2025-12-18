package br.com.OrderTrack.Order.infrastructure.scheduler;

import br.com.OrderTrack.Order.application.useCase.CancelOrderUseCase;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTimeoutScheduler {

    private final JPAOrderRepository orderRepository;
    private final CancelOrderUseCase cancelOrderUseCase;

    @Scheduled(fixedDelay = 60000)
    @SchedulerLock(name = "OrderTimeoutScheduler_checkTimeouts", lockAtLeastFor = "PT30S", lockAtMostFor = "PT1M")
    @Transactional
    public void checkTimeouts() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(30); // 30 min de tolerância

        var expiredOrders = orderRepository.findByStatusAndOrderDateBefore(OrderStatus.PENDING_PAYMENT, cutoff);

        for (var order : expiredOrders) {
            log.warn("Pedido {} expirou (timeout). Iniciando cancelamento compensatório.", order.getId());
            try {
                cancelOrderUseCase.execute(order.getId());
            } catch (Exception e) {
                log.error("Falha ao cancelar pedido expirado {}", order.getId(), e);
            }
        }
    }
}