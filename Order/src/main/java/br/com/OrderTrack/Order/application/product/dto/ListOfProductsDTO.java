package br.com.OrderTrack.Order.application.product.dto;

import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;

import java.math.BigDecimal;

public record ListOfProductsDTO(Long id, String name, String category, BigDecimal price) {
    public ListOfProductsDTO(ProductEntity productEntity){
        this(productEntity.getId(), productEntity.getName(), productEntity.getCategory(), productEntity.getPrice());
    }
}
