package br.com.OrderTrack.Order.infrastructure.valueObject;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users_profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@IdClass(UserProfileIDEntity.class)
public class UserProfileEntity {

    @Id
    private UUID userId;

    @Id
    private UUID profileId;
}
