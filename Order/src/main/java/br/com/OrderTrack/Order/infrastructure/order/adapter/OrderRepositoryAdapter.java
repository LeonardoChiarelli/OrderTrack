package br.com.OrderTrack.Order.infrastructure.order.adapter;

import br.com.OrderTrack.Order.application.port.out.OrderGateway;
import br.com.OrderTrack.Order.domain.order.Order;
import br.com.OrderTrack.Order.infrastructure.order.persistence.mapper.OrderEntityMapper;
import br.com.OrderTrack.Order.infrastructure.order.persistence.repository.JPAOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderGateway {

    private final JPAOrderRepository jpaRepository;
    private final OrderEntityMapper mapper;

    @Override
    public Order save(Order order) {
        var entity = mapper.toEntity(order);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}
