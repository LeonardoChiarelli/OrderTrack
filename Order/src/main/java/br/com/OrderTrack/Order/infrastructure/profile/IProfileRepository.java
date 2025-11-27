package br.com.OrderTrack.Order.infrastructure.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfileRepository extends JpaRepository<ProfileEntity, Long> {
    Optional<ProfileEntity> findByName(String roleConsumer);
}
