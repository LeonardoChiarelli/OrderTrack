package br.com.OrderTrack.Order.infrastructure.inventory.adapter;

import br.com.OrderTrack.Order.application.port.out.InventoryGateway;
import br.com.OrderTrack.Order.domain.inventory.Inventory;
import br.com.OrderTrack.Order.infrastructure.inventory.JpaInventoryRepository;
import br.com.OrderTrack.Order.infrastructure.inventory.gateways.InventoryEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryGateway {


    private final JpaInventoryRepository repository;
    private final InventoryEntityMapper mapper;

    @Override
    public Optional<Inventory> findByProductNameWithLock(String productName) {
        return repository.findByProductNameWithLock(productName)
                .map(mapper::toDomain);
    }

    @Override
    public void save(Inventory inventory) {
        var entity = mapper.toEntity(inventory);
        repository.save(entity);
    }
}
