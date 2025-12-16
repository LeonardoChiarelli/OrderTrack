package br.com.OrderTrack.Product.infrastructure.product.gateways;

import br.com.OrderTrack.Order.domain.product.Product;
import br.com.OrderTrack.Product.infrastructure.product.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(product);
    }

    public Product toDomain(ProductEntity productEntity) {
        return Product.builder()
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .category(productEntity.getCategory())
                .price(productEntity.getPrice())
            .build();
    }
}
