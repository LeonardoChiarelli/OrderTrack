package br.com.OrderTrack.Payment.domain.repository;

import br.com.OrderTrack.Payment.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Long> {
}
