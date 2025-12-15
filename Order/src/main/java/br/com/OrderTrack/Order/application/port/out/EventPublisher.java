package br.com.OrderTrack.Order.application.port.out;

import org.springframework.stereotype.Component;

@Component
public interface EventPublisher {
    void publish(Object event);
}
