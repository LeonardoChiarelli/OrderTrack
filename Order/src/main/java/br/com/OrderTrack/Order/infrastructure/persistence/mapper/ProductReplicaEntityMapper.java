package br.com.OrderTrack.Order.infrastructure.persistence.mapper;

import br.com.OrderTrack.Order.domain.model.valueObject.Product;
import br.com.OrderTrack.Order.infrastructure.persistence.entity.ProductReplicaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductReplicaEntityMapper {

    public ProductReplicaEntity toEntity(Product product) {
        return new ProductReplicaEntity(product.getId(), product.getName(), product.getPrice(), product.isActive());
    }

    public Product toDomain(ProductReplicaEntity productReplicaEntity) {
        return Product.builder()
                .id(productReplicaEntity.getId())
                .name(productReplicaEntity.getName())
                .price(productReplicaEntity.getPrice())
                .active(productReplicaEntity.isActive())
            .build();
    }
}
