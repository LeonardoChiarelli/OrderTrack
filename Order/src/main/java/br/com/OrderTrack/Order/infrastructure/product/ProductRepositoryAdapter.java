package br.com.OrderTrack.Order.infrastructure.product;

import br.com.OrderTrack.Order.application.port.out.ProductGateway;
import br.com.OrderTrack.Order.domain.product.Product;
import br.com.OrderTrack.Order.infrastructure.product.gateways.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductGateway {

    private final JpaProductRepository repository;
    private final ProductEntityMapper mapper;

    @Override
    public Optional<Product> findByName(String name) {
        return repository.findByName(name)
                .map(mapper::toDomain);
    }
}
