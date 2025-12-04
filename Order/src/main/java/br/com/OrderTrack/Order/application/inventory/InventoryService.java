package br.com.OrderTrack.Order.application.inventory;

import br.com.OrderTrack.Order.application.inventory.dto.InventoryListDTO;
import br.com.OrderTrack.Order.application.inventory.dto.UpdateInventoryDTO;
import br.com.OrderTrack.Order.infrastructure.inventory.InventoryEntity;
import br.com.OrderTrack.Order.infrastructure.inventory.IInventoryRepository;
import br.com.OrderTrack.Order.domain.exception.ValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private IInventoryRepository repository;

    public Page<InventoryListDTO> getList(Pageable pageable) {
        return repository.findAll(pageable).map(InventoryListDTO::new);
    }

    public void updateQuantity(@Valid UpdateInventoryDTO dto, String requestURL) {
        try {
            var id = Long.parseLong(dto.productIdOrNumber());
            var inventory = repository.findById(id).orElseThrow(() -> new ValidationException("ProductEntity not found"));

            doValidationsAndAddOrDecrease(inventory, requestURL, dto.quantity());
        } catch (NumberFormatException e) {
            var inventory = repository.findByProductName(dto.productIdOrNumber()).orElseThrow(() -> new ValidationException("ProductEntity not found"));

            doValidationsAndAddOrDecrease(inventory, requestURL, dto.quantity());
        }
    }

    public void doValidationsAndAddOrDecrease(InventoryEntity inventoryEntity, String requestURL, Integer quantity) {
        if (!inventoryEntity.getProductEntity().isActive()) { throw new ValidationException("ProductEntity is not active"); }
        if (requestURL.equals("http://localhost:8080/orderTrack/admin/inventory/add")) { inventoryEntity.addQuantity(quantity); }
        if (requestURL.equals("http://localhost:8080/orderTrack/inventory/decrease")) { inventoryEntity.decreaseQuantity(quantity); }
    }
}
