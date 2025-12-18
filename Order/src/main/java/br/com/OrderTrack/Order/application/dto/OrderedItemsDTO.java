package br.com.OrderTrack.Order.application.dto;

import br.com.OrderTrack.Order.domain.model.OrderItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderedItemsDTO(
        UUID id,

        @NotBlank
        UUID productId,

        @NotNull
        @Positive
        Integer quantity
) {
        public OrderedItemsDTO(OrderItem orderItem) {
                this(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity());
        }
}
