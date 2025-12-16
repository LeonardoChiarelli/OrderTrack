package br.com.OrderTrack.Product.infrastructure.product;

import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    Boolean existsByName(@NotBlank String nome);

    Page<ProductEntity> findAllByActiveTrue(Pageable pageable);

    @Modifying
    void deleteById(@NotNull Long id);

    Optional<ProductEntity> findByName(@NotBlank String name);
}
