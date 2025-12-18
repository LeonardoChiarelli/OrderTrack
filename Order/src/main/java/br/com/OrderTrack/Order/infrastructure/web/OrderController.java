package br.com.OrderTrack.Order.infrastructure.web;

import br.com.OrderTrack.Common.security.UserPrincipal;
import br.com.OrderTrack.Order.application.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.application.dto.OrderDetailsDTO;
import br.com.OrderTrack.Order.application.useCase.UpdateOrderStatusUseCase;
import br.com.OrderTrack.Order.domain.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.domain.model.OrderStatus;
import br.com.OrderTrack.Order.domain.port.in.CreateOrderInputPort;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.infrastructure.persistence.mapper.OrderEntityMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("orderTrack/order")
public class OrderController {

    @Autowired
    private CreateOrderInputPort createOrderUseCase;

    @Autowired
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Autowired
    private OrderGateway orderGateway;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Void> creteOrder(
            @RequestBody @Valid CreateOrderDTO dto,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal UserPrincipal user
    ){
        UUID orderId = createOrderUseCase.execute(dto, user.email(), user.name());
        var uri = uriBuilder.path("/orderTrack/order/{id}").buildAndExpand(orderId).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable UUID id){
        var order = orderGateway.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        return ResponseEntity.ok(new OrderDetailsDTO(order));
    }

    @PatchMapping("/admin/{id}/prepare")
    public ResponseEntity<Void> markAsPreparing(@PathVariable UUID id) {
        updateOrderStatusUseCase.execute(id, OrderStatus.PREPARING);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/{id}/ship")
    public ResponseEntity<Void> markAsShipped(@PathVariable UUID id) {
        updateOrderStatusUseCase.execute(id, OrderStatus.SHIPPED);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/{id}/deliver")
    public ResponseEntity<Void> markAsDelivered(@PathVariable UUID id) {
        updateOrderStatusUseCase.execute(id, OrderStatus.DELIVERED);
        return ResponseEntity.ok().build();
    }
}
