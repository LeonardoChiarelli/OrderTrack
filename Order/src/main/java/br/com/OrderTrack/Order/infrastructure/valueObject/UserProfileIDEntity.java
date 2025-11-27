package br.com.OrderTrack.Order.infrastructure.valueObject;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserProfileIDEntity {
    private Long idUser;
    private Long idProfile;
}
