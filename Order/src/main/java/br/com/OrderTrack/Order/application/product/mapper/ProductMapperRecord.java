package br.com.OrderTrack.Order.application.product.mapper;

import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;

import java.math.BigDecimal;

public record ProductMapperRecord(Long id, String name, BigDecimal price) {
    public ProductMapperRecord(ProductEntity p){
        this(p.getId(), p.getName(), p.getPrice());
    }
}
