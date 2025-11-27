package br.com.OrderTrack.Order.application.order;

import br.com.OrderTrack.Order.application.order.dto.ChangeOrderStatus;
import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.application.order.dto.OrderDetailsDTO;
import br.com.OrderTrack.Order.application.order.dto.OrderedItemsDTO;
import br.com.OrderTrack.Order.application.helper.HelperMethod;
import br.com.OrderTrack.Order.infrastructure.order.valueObject.Address;
import br.com.OrderTrack.Order.infrastructure.order.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.order.OrderItemEntity;
import br.com.OrderTrack.Order.infrastructure.inventory.IInventoryRepository;
import br.com.OrderTrack.Order.infrastructure.order.IOrderRepository;
import br.com.OrderTrack.Order.application.exception.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private IOrderRepository repository;

    @Autowired
    private IInventoryRepository inventoryRepository;

    public OrderEntity createOrder(@Valid CreateOrderDTO dto) {

        var address = new Address(dto.shippingAddress());
        var totalPrice = getTotalPrice(dto.items());
        var orderedItems = getItems(dto.items());

        return new OrderEntity(dto, address, totalPrice, orderedItems);
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

    public List<OrderItemEntity> getItems(List<OrderedItemsDTO> itemsDTO){
        return itemsDTO.stream().map(item-> {
            var inventory = inventoryRepository.findByProductName(item.productName()).orElseThrow(() -> new ValidationException("ProductEntity not found"));
            var product = HelperMethod.loadProductsByName(item.productName());

            if (inventory.getQuantity() <= item.quantity() && !product.isActive()) { throw new ValidationException("ProductEntity was not active or out of stock"); }
            inventory.decreaseQuantity(item.quantity());

            return new OrderItemEntity(item, product);
        }).collect(Collectors.toList());
    }

    public BigDecimal getTotalPrice(List<OrderedItemsDTO> itemsDTO){
        return itemsDTO.stream()
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

