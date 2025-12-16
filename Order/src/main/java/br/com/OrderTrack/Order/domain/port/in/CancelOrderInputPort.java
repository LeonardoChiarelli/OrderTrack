package br.com.OrderTrack.Order.domain.port.in;

import br.com.OrderTrack.Order.application.dto.CreateOrderDTO;

import java.util.UUID;

public interface CancelOrderInputPort {
    UUID execute(CreateOrderDTO dto, String userEmail, String userName);
}
