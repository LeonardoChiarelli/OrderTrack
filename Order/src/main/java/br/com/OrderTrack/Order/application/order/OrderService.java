package br.com.OrderTrack.Order.application.order;

import br.com.OrderTrack.Order.application.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.application.exception.InventoryException;
import br.com.OrderTrack.Order.application.order.dto.ChangeOrderStatus;
import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.application.order.dto.OrderDetailsDTO;
import br.com.OrderTrack.Order.application.order.port.out.OrderEventPublisherPort;
import br.com.OrderTrack.Order.domain.exception.ValidationException;
import br.com.OrderTrack.Order.domain.order.event.PaymentRequestedEvent;
import br.com.OrderTrack.Order.infrastructure.inventory.IInventoryRepository;
import br.com.OrderTrack.Order.infrastructure.order.persistence.repository.JPAOrderRepository;
import br.com.OrderTrack.Order.infrastructure.order.persistence.entity.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.order.persistence.entity.OrderItemEntity;
import br.com.OrderTrack.Order.infrastructure.order.persistence.mapper.OrderEntityMapper;
import br.com.OrderTrack.Order.infrastructure.order.persistence.entity.valueObject.AddressEntity;
import br.com.OrderTrack.Order.infrastructure.product.IProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private JPAOrderRepository repository;

    @Autowired
    private IInventoryRepository inventoryRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private OrderEventPublisherPort orderEventPublisherPort;

    @Autowired
    private OrderEntityMapper mapper;

    public OrderEntity createOrder(@Valid CreateOrderDTO dto) {
        List<OrderItemEntity> orderItems = new ArrayList<>();

        dto.items().stream()
                .map(i -> {
                    i.productsName().forEach(productName -> {
                       var product = productRepository.findByName(productName).orElseThrow(() -> new EntityNotFoundException("Product Not Found"));

                       var inventory = inventoryRepository.findByProductName(product.getName()).orElseThrow(() -> new EntityNotFoundException("Inventory not found"));

                       if (inventory.getQuantity() < i.quantity()) {
                           throw new InventoryException(String.format("Product '%s' is out of stock.", inventory.getProductEntity().getName()));
                       }

                       var orderItem = new OrderItemEntity(i, product);
                        orderItems.add(orderItem);
                    });
                    return null;
                });

        var orderEntity = new OrderEntity(dto, new AddressEntity(dto.shippingAddress()), orderItems);
        var orderDomain = mapper.toDomain(orderEntity);

        var event = PaymentRequestedEvent.builder()
                .orderId(orderDomain.getId())
                .totalPrice(orderDomain.getTotalPrice())
                .currency("BRL")
            .build();

        orderEventPublisherPort.publish(event);

        return orderEntity;
    }

    public OrderEntity changeStatus(@Valid ChangeOrderStatus dto) {
        var order = repository.findById(dto.id()).orElseThrow(() -> new ValidationException("OrderEntity not found"));
        order.changeStatus(dto.status());
        return order;
    }

    public Page<OrderDetailsDTO> listAllOrders(Pageable pageable) {
        return repository.findAll(pageable).map(OrderDetailsDTO::new);
    }

    public OrderEntity getOrder(Long id) {
        return repository.findById(id).orElseThrow(() -> new ValidationException("OrderEntity not found"));
    }
}

