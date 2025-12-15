package br.com.OrderTrack.Order.application.order.useCase;

import br.com.OrderTrack.Order.application.order.port.in.CreateOrderInputPort;
import br.com.OrderTrack.Order.application.order.port.out.OrderEventPublisherPort;
import br.com.OrderTrack.Order.application.order.port.out.OrderRepositoryPort;

import java.util.UUID;

public class CreateOrderUseCase implements CreateOrderInputPort {

    private final OrderRepositoryPort repository;
    private final OrderEventPublisherPort eventPublisher;

    public CreateOrderUseCase(
            OrderRepositoryPort repository,
            OrderEventPublisherPort eventPublisher
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UUID execute() {
        var order =
    }
}
