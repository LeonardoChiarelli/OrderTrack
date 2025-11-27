package br.com.OrderTrack.Track.domain.dto;

import br.com.OrderTrack.Track.domain.model.Address;
import br.com.OrderTrack.Track.domain.model.Order;
import br.com.OrderTrack.Track.domain.model.OrderStatus;

public record OrderDTO(Long id, OrderStatus orderStatus, Address shippingAddress) {
    public OrderDTO(Order order){
        this(order.getId(), order.getStatus(), order.getShippingAddress());
    }
}
