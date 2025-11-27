package br.com.OrderTrack.Order.application.product.dto;

import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;

import java.math.BigDecimal;

public record ProductDetailsDTO(Long id, String name, String description, String category, BigDecimal price, String status) {

    public ProductDetailsDTO(ProductEntity productEntity, String status){
        this(productEntity.getId(), productEntity.getName(), productEntity.getDescription(), productEntity.getCategory(), productEntity.getPrice(), status);
    }
}
