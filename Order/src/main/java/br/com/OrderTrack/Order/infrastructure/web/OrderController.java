package br.com.OrderTrack.Order.infrastructure.web;

import br.com.OrderTrack.Order.application.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.application.dto.OrderDetailsDTO;
import br.com.OrderTrack.Order.application.order.port.in.CreateOrderInputPort;
import br.com.OrderTrack.Order.infrastructure.user.UserEntity;
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

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<OrderDetailsDTO> creteOrder(
            @RequestBody @Valid CreateOrderDTO dto,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal UserEntity user
    ){
        UUID orderId = createOrderUseCase.execute(dto, user.getEmail(), user.getName());
        var uri = uriBuilder.path("/orderTrack/order/{id}").buildAndExpand(orderId).toUri();
        return ResponseEntity.created(uri).build();
    }
}
