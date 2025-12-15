package br.com.OrderTrack.Order.application.order.port.out;

import org.springframework.stereotype.Component;

@Component
public interface OrderEventPublisherPort {
    void publish(Object event);
}
