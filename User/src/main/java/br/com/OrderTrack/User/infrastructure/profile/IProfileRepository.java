package br.com.OrderTrack.User.infrastructure.profile;

import br.com.OrderTrack.Common.infrastructure.profile.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfileRepository extends JpaRepository<br.com.OrderTrack.Common.infrastructure.profile.ProfileEntity, Long> {
    Optional<ProfileEntity> findByName(String roleConsumer);
}
