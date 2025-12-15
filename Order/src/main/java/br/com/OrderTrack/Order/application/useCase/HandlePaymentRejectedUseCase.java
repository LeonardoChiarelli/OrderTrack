package br.com.OrderTrack.Order.application.useCase;

import br.com.OrderTrack.Order.application.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.application.inventory.InventoryService;
import br.com.OrderTrack.Order.application.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.order.Order;
import br.com.OrderTrack.Order.domain.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + orderId));

        if (order.getStatus() == OrderStatus.CANCELED) {
            return;
        }

        order.markAsCanceled();
        repository.save(order);

        order.getItems().forEach(item -> {
            inventoryService.addStock(
                    item.getProduct().getName(), // Ou usar ID se refatorar o findByName
                    item.getQuantity()
            );
        });

        System.out.println("Order " + orderId + " canceled and items returned to inventory.");
    }
}
