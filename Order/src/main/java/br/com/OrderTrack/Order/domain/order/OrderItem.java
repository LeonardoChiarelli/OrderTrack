package br.com.OrderTrack.Order.domain.order;

import br.com.OrderTrack.Order.domain.product.Product;

import java.util.UUID;

public class OrderItem {

    private final UUID id;
    private final Product product;
}
