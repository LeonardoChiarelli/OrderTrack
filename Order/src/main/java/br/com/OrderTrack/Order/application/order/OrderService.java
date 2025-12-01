package br.com.OrderTrack.Order.application.order;

import br.com.OrderTrack.Order.application.exception.EntityNotFoundException;
import br.com.OrderTrack.Order.application.exception.InventoryException;
import br.com.OrderTrack.Order.application.exception.ValidationException;
import br.com.OrderTrack.Order.application.helper.HelperMethod;
import br.com.OrderTrack.Order.application.order.dto.ChangeOrderStatus;
import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;
import br.com.OrderTrack.Order.application.order.dto.OrderDetailsDTO;
import br.com.OrderTrack.Order.application.order.dto.OrderedItemsDTO;
import br.com.OrderTrack.Order.infrastructure.inventory.IInventoryRepository;
import br.com.OrderTrack.Order.infrastructure.order.IOrderRepository;
import br.com.OrderTrack.Order.infrastructure.order.OrderEntity;
import br.com.OrderTrack.Order.infrastructure.order.OrderItemEntity;
import br.com.OrderTrack.Order.infrastructure.order.valueObject.AddressEntity;
import br.com.OrderTrack.Order.infrastructure.product.IProductRepository;
import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private IOrderRepository repository;

    @Autowired
    private IInventoryRepository inventoryRepository;

    @Autowired
    private IProductRepository productRepository;

    public OrderEntity createOrder(@Valid CreateOrderDTO dto) {

        var address = new AddressEntity(dto.shippingAddress());
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

    public List<OrderItemEntity> getItems(List<OrderedItemsDTO> itemsDTO) {
        var productList = new ArrayList<ProductEntity>();
        var orderItemEntityList = new ArrayList<OrderItemEntity>();

        itemsDTO.stream().map(item ->
                item.productsName().stream().map(productName -> {
                    var inventory = inventoryRepository.findByProductName(productName).orElseThrow(() -> new ValidationException("ProductEntity not found"));
                    var product = HelperMethod.loadProductsByName(productName);
                    productList.add(product);

                    if (inventory.getQuantity() <= item.quantity() && !product.isActive()) {
                        throw new ValidationException("ProductEntity was not active or out of stock");
                    }
                    inventory.decreaseQuantity(item.quantity());

                    var orderItemEntity = new OrderItemEntity(item, productList);
                    orderItemEntityList.add(orderItemEntity);
                    return orderItemEntity;
                }));
        return orderItemEntityList;
    }

    public BigDecimal getTotalPrice(List<OrderedItemsDTO> itemsDTO) {
        return (BigDecimal) itemsDTO.stream()
                .map(item -> {
                    var product = productRepository
                            .findByName(item.productName())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found."));

                    var inventory = inventoryRepository
                            .findByProductName(product.getName())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found."))
                            .getQuantity();

                    if (inventory <= 0) {
                        throw new InventoryException("The product is out of stock.");
                    }

                    return product.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
                });
    }
}

