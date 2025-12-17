package br.com.OrderTrack.Order.infrastructure.configuration;

import br.com.OrderTrack.Order.application.useCase.*;
import br.com.OrderTrack.Order.domain.port.out.EventPublisherPort;
import br.com.OrderTrack.Order.domain.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.port.out.ProductGateway;
import br.com.OrderTrack.Order.infrastructure.client.CatalogClientAdapter;
import br.com.OrderTrack.Order.infrastructure.messaging.adapter.RabbitEventPublisherAdapter;
import br.com.OrderTrack.Order.infrastructure.persistence.mapper.ProductReplicaEntityMapper;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAProductReplicaRepository;
import io.micrometer.core.instrument.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderGateway orderGateway, ProductGateway productGateway, RabbitEventPublisherAdapter eventPublisher, Counter orderCreatedCounter) {
        return new CreateOrderUseCase(orderGateway, productGateway, eventPublisher, orderCreatedCounter);
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

    @Bean
    public CatalogClientAdapter catalogClientAdapter(RestTemplate restTemplate, JPAProductReplicaRepository repository,  ProductReplicaEntityMapper mapper) {
        return new CatalogClientAdapter(restTemplate, repository, mapper);
    }
}