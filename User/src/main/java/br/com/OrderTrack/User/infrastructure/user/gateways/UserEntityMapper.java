package br.com.OrderTrack.User.infrastructure.user.gateways;

import br.com.OrderTrack.Order.domain.user.User;
import br.com.OrderTrack.Common.infrastructure.profile.gateways.ProfileEntityMapper;
import br.com.OrderTrack.Common.infrastructure.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    @Autowired
    private ProfileEntityMapper profileEntityMapper;

    public UserEntity toEntity(User user) {
        return new UserEntity(user, user.getProfiles().stream()
                .map(p -> profileEntityMapper.toEntity(p))
                .toList());
    }

    public User toDomain(UserEntity userEntity) {
        return User.builder()
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .profile(userEntity.getProfileEntities().stream()
                        .map(p -> profileEntityMapper.toDomain(p))
                        .toList())
            .build();
    }
}
