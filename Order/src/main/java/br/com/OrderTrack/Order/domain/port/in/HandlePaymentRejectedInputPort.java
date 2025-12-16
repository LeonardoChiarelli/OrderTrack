package br.com.OrderTrack.Order.domain.port.in;

import java.util.UUID;

public interface HandlePaymentRejectedInputPort {
    void execute(UUID orderId);
}
