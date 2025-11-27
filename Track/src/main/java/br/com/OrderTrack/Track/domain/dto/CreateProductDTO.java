package br.com.OrderTrack.Track.domain.dto;

import java.math.BigDecimal;

public record CreateProductDTO(

    String name,
    String description,
    String category,
    BigDecimal price
) {
}
