package br.com.OrderTrack.Order.infrastructure.configuration;

import br.com.OrderTrack.Order.application.useCase.*;
import br.com.OrderTrack.Order.domain.port.out.EventPublisherPort;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.port.out.ProductGateway;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderGateway orderGateway, ProductGateway productGateway, RabbitEventPublisherAdapter eventPublisher) {
        return new CreateOrderUseCase(orderGateway, productGateway, eventPublisher);
    }

    @Bean
    public ApproveOrderStockUseCase approveOrderStockUseCase(OrderGateway orderGateway, RabbitEventPublisherAdapter eventPublisher) {
        return new ApproveOrderStockUseCase(orderGateway, eventPublisher);
    }

    @Bean
    public CancelOrderUseCase cancelOrderUseCase(OrderGateway orderGateway, RabbitEventPublisherAdapter eventPublisher) {
        return new CancelOrderUseCase(orderGateway, eventPublisher);
    }

    @Bean
    public HandlePaymentApprovedUseCase handlePaymentApprovedUseCase(OrderGateway orderGateway, RabbitEventPublisherAdapter eventPublisher) {
        return new HandlePaymentApprovedUseCase(orderGateway, eventPublisher);
    }

    @Bean
    public HandlePaymentRejectedUseCase handlePaymentRejectedUseCase(OrderGateway orderGateway, RabbitEventPublisherAdapter eventPublisher) {
        return new HandlePaymentRejectedUseCase(orderGateway, eventPublisher);
    }
}