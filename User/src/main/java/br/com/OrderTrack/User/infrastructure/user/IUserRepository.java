package br.com.OrderTrack.User.infrastructure.user;

import br.com.OrderTrack.Common.infrastructure.user.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<br.com.OrderTrack.Common.infrastructure.user.UserEntity, Long> {

    Boolean existsByEmail(@NotBlank @Email String email);

    Optional<UserEntity> findByEmail(String email);
}
