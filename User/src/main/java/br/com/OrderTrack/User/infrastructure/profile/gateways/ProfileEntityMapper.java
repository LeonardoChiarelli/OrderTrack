package br.com.OrderTrack.User.infrastructure.profile.gateways;

import br.com.OrderTrack.Order.domain.profile.Profile;
import br.com.OrderTrack.User.infrastructure.profile.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileEntityMapper {

    public ProfileEntity toEntity(Profile profile) {
        return new ProfileEntity(profile);
    }

    public Profile toDomain(ProfileEntity profileEntity) {
        return Profile.builder()
                .name(profileEntity.getName())
            .build();
    }
}
