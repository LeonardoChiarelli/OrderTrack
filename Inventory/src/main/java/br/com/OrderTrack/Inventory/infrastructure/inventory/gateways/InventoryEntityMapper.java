package br.com.OrderTrack.Inventory.infrastructure.inventory.gateways;

import br.com.OrderTrack.Order.domain.inventory.Inventory;
import br.com.OrderTrack.Inventory.infrastructure.inventory.InventoryEntity;
import br.com.OrderTrack.Order.infrastructure.product.gateways.ProductEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryEntityMapper {

    @Autowired
    private ProductEntityMapper productEntityMapper;

    public InventoryEntity toEntity(Inventory inventory) {
        return new InventoryEntity(inventory, productEntityMapper.toEntity(inventory.getProduct()));
    }

    public Inventory toDomain(InventoryEntity inventory) {
        return Inventory.builder()
                .quantity(inventory.getQuantity())
                .product(productEntityMapper.toDomain(inventory.getProductEntity()))
            .build();
    }
}
