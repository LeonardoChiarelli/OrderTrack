package br.com.OrderTrack.Order.application.order.port.in;

import br.com.OrderTrack.Order.application.order.dto.CreateOrderDTO;

import java.util.UUID;

public interface CreateOrderInputPort {
    UUID execute(CreateOrderDTO dto);
}
