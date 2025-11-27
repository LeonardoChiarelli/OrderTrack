package br.com.OrderTrack.Order.application.inventory.dto;

import br.com.OrderTrack.Order.infrastructure.inventory.InventoryEntity;

public record InventoryListDTO(Long productId, String productName, Integer quantity) {
    public InventoryListDTO(InventoryEntity inventoryEntity){
        this(inventoryEntity.getProductEntity().getId(), inventoryEntity.getProductEntity().getName(), inventoryEntity.getQuantity());
    }
}
