package br.com.OrderTrack.Order.application.port.out;

import br.com.OrderTrack.Order.domain.inventory.Inventory;

import java.util.Optional;

public interface InventoryGateway {

    Optional<Inventory> findByProductNameWithLock(String productName);
    void save(Inventory inventory);
}
