package br.com.OrderTrack.User.infrastructure.valueObject.gateways;

import br.com.OrderTrack.Order.domain.valueObject.UserProfile;
import br.com.OrderTrack.Common.infrastructure.valueObject.UserProfileEntity;

public class UserProfileEntityMapper {

    public UserProfileEntity toEntity(UserProfile userProfile) {
        return new UserProfileEntity(userProfile);
    }

    public UserProfile toDomain(UserProfileEntity userProfileEntity) {
        return UserProfile.builder()
                .userId(userProfileEntity.getUserId())
                .profileId(userProfileEntity.getProfileId())
            .build();
    }
}
