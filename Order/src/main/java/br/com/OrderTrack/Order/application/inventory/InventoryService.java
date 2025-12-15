package br.com.OrderTrack.Order.application.inventory;

import br.com.OrderTrack.Order.application.inventory.dto.InventoryListDTO;
import br.com.OrderTrack.Order.application.inventory.dto.UpdateInventoryDTO;
import br.com.OrderTrack.Order.infrastructure.inventory.InventoryEntity;
import br.com.OrderTrack.Order.infrastructure.inventory.JpaInventoryRepository;
import br.com.OrderTrack.Order.domain.exception.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    @Autowired
    private JpaInventoryRepository repository;

    public Page<InventoryListDTO> getList(Pageable pageable) {
        return repository.findAll(pageable).map(InventoryListDTO::new);
    }

    @Transactional
    public void addStock(String productIdOrNumber, Integer quantity) {
        var inventory = findInventory(productIdOrNumber);
        if (!inventory.getProductEntity().isActive()) {
            throw new ValidationException("ProductEntity is not active");
        }
        inventory.addQuantity(quantity);
        repository.save(inventory);
    }

    @Transactional
    public void decreaseStock(String productIdOrNumber, Integer quantity) {
        var inventory = findInventory(productIdOrNumber);
        if (!inventory.getProductEntity().isActive()) {
            throw new ValidationException("ProductEntity is not active");
        }
        inventory.decreaseQuantity(quantity);
        repository.save(inventory);
    }

    private InventoryEntity findInventory(String productIdOrNumber) {
        try {
            var id = Long.parseLong(productIdOrNumber);
            return repository.findById(id)
                    .orElseThrow(() -> new ValidationException("Inventory not found for ID: " + id));
        } catch (NumberFormatException e) {
            return repository.findByProductName(productIdOrNumber)
                    .orElseThrow(() -> new ValidationException("Inventory not found for Product: " + productIdOrNumber));
        }
    }
}
