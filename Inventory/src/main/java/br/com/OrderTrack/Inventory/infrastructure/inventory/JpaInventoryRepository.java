package br.com.OrderTrack.Inventory.infrastructure.inventory;

import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaInventoryRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByProductName(@NotBlank String productName);

    @Query("SELECT product p FROM InventoryEntity")
    Page<ProductEntity> findAllProducts(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // O segredo está aqui!
    @Query("SELECT i FROM InventoryEntity i WHERE i.productEntity.name = :productName")
    Optional<InventoryEntity> findByProductNameWithLock(String productName);
}
