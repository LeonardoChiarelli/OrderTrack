package br.com.OrderTrack.Order.infrastructure.client;

import br.com.OrderTrack.Order.domain.port.out.ProductGateway;
import br.com.OrderTrack.Order.domain.model.valueObject.Product;
import br.com.OrderTrack.Order.infrastructure.persistence.mapper.ProductReplicaEntityMapper;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAProductReplicaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component("catalogClientAdapter")
@RequiredArgsConstructor
public class CatalogClientAdapter implements ProductGateway {

    private final RestTemplate rt;
    private final JPAProductReplicaRepository repository;
    private final ProductReplicaEntityMapper mapper;

    @Value("${app.services.product-url:http://product-service:8086}")
    private String productServiceUrl;

    @Override
    @CircuitBreaker(name = "ProductService", fallbackMethod = "fallbackFindById")
    public Optional<Product> findById(UUID id) {
        String url = UriComponentsBuilder
                .fromPath(productServiceUrl + "/orderTrack/product/" + id)
                .toUriString();

        var response = rt.getForObject(url, ProductDetailsResponse.class);

        if (response == null) return Optional.empty();

        return Optional.of(Product.builder()
                .id(response.id())
                .name(response.name())
                .price(response.price())
                .active(true)
                .build());
    }

    public Optional<Product> fallbackFindById(UUID id, Throwable t) {
        log.error("Erro ao comunicar com Product Service {}. Adicionando Fallback.", t.getMessage());
        return repository.findById(id).map(mapper::toDomain);
    }

    private record ProductDetailsResponse(UUID id, String name, BigDecimal price, boolean active) {}
}