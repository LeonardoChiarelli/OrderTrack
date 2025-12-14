package br.com.OrderTrack.Track.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tracks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private Address shippingAddress;

    private LocalDateTime updatedAt;
}
