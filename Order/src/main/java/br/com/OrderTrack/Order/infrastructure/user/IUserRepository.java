package br.com.OrderTrack.Order.infrastructure.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByEmail(@NotBlank @Email String email);

    Optional<UserEntity> findByEmail(String email);
}
