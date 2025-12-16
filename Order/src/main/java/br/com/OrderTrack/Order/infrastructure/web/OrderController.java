package br.com.OrderTrack.Order.infrastructure.web;

import br.com.OrderTrack.Common.security.UserPrincipal;
import br.com.OrderTrack.Order.application.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.domain.port.in.CreateOrderInputPort;
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
    public ResponseEntity<Void> creteOrder(
            @RequestBody @Valid CreateOrderDTO dto,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal UserPrincipal user
    ){
        UUID orderId = createOrderUseCase.execute(dto, user.email(), user.name());
        var uri = uriBuilder.path("/orderTrack/order/{id}").buildAndExpand(orderId).toUri();

        return ResponseEntity.created(uri).build();
    }
}
