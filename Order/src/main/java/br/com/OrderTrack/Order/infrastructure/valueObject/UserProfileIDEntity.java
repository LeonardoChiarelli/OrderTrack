package br.com.OrderTrack.Order.infrastructure.valueObject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserProfileIDEntity {
    private UUID idUser;
    private UUID idProfile;
}
