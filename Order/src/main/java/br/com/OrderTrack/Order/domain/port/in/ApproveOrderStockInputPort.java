package br.com.OrderTrack.Order.domain.port.in;

import java.util.UUID;

public interface ApproveOrderStockInputPort {
    void execute(UUID orderId);
}
