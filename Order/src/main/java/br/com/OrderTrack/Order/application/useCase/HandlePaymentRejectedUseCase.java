package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.application.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.application.inventory.InventoryService;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.model.Order;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class HandlePaymentRejectedUseCase {
    private final OrderGateway repository;
    private final InventoryService inventoryService;

    public HandlePaymentRejectedUseCase(OrderGateway repository, InventoryService inventoryService) {
        this.repository = repository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public void execute(UUID orderId) {
        log.info("Processando rejeição de pagamento para o pedido: {}", orderId);

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + orderId));

        if (order.getStatus() == OrderStatus.CANCELED) {
            log.warn("Pedido {} já está cancelado. Ignorando evento.", orderId);
            return;
        }

        order.markAsCanceled();
        repository.save(order);

        order.getItems().forEach(item -> {
            try {
                inventoryService.addStock(
                        item.getProduct().getName(), // Ou usar ID se refatorar o findByName
                        item.getQuantity()
                );
            } catch (Exception e) {
                log.error("Falha crítica ao devolver estoque do produto {} no pedido {}", item.getProduct().getName(), orderId);
                throw e;
            }
        });

        log.info("Order {} canceled and items returned to inventory.", orderId);
    }
}
