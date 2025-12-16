package br.com.OrderTrack.Product.application.product.dto;

import br.com.OrderTrack.Product.infrastructure.product.ProductEntity;
import br.com.OrderTrack.Product.domain.product.Product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDetailsDTO(UUID id, String name, String description, String category, BigDecimal price, String status) {

    public ProductDetailsDTO(ProductEntity productEntity, String status){
        this(productEntity.getId(), productEntity.getName(), productEntity.getDescription(), productEntity.getCategory(), productEntity.getPrice(), status);
    }
}
