package br.com.OrderTrack.Order.domain.port.out;

public interface EventPublisherPort {
    void publish(Object event, String eventType, String aggregateId);
}
