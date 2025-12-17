package br.com.OrderTrack.Order.infrastructure.persistence.adapter;

import br.com.OrderTrack.Order.domain.model.valueObject.Product;
import br.com.OrderTrack.Order.domain.port.out.ProductGateway;
import br.com.OrderTrack.Order.infrastructure.persistence.mapper.ProductReplicaEntityMapper;
import br.com.OrderTrack.Order.infrastructure.persistence.repository.JPAProductReplicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductReplicaAdapter implements ProductGateway {

    private final JPAProductReplicaRepository jpaRepository;
    private final ProductReplicaEntityMapper mapper;

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
}