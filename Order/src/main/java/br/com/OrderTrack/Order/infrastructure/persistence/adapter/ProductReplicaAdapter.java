package br.com.OrderTrack.Order.infrastructure.persistence.adapter;

import br.com.OrderTrack.Order.domain.model.valueObject.Product;
import br.com.OrderTrack.Order.domain.port.out.ProductGateway;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProductReplicaEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface ProductReplicaRepository extends JpaRepository<ProductReplicaEntity, UUID> {}

@Component
@Primary
public class ProductReplicaAdapter implements ProductGateway {

    private final ProductReplicaRepository repository;

    public ProductReplicaAdapter(ProductReplicaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id)
                .map(entity -> Product.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .price(entity.getPrice())
                        .active(entity.isActive())
                        .build());
    }
}