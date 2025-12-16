package br.com.OrderTrack.Order.domain.port.in;

import java.util.UUID;

public interface HandlePaymentApprovedInputPort {
    void execute(UUID orderId);
}
