package br.com.OrderTrack.Order.application.order;

import br.com.OrderTrack.Order.application.order.dto.ChangeOrderStatus;
import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.application.order.dto.OrderDetailsDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("orderTrack/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<OrderDetailsDTO> creteOrder(@RequestBody @Valid CreateOrderDTO dto, UriComponentsBuilder uriBuilder){
        var order = service.createOrder(dto);

        var uri = uriBuilder.path("{id}").buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(uri).body(new OrderDetailsDTO(order));
    }

    @GetMapping("/admin/listAll")
    public ResponseEntity<Page<OrderDetailsDTO>> listAll(Pageable pageable){
        return ResponseEntity.ok(service.listAllOrders(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable Long id){
        return ResponseEntity.ok(new OrderDetailsDTO(service.getOrder(id)));
    }

    @PatchMapping("/admin/changeStatus")
    @Transactional
    public ResponseEntity<OrderDetailsDTO> changeOrderStatus(@RequestBody @Valid ChangeOrderStatus dto){
        var order = service.changeStatus(dto);

        return ResponseEntity.ok(new OrderDetailsDTO(order));
    }
}
