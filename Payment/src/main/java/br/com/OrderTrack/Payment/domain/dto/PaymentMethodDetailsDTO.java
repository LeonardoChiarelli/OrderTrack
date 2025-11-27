package br.com.OrderTrack.Payment.domain.dto;

import br.com.OrderTrack.Payment.domain.model.PaymentMethod;

public record PaymentMethodDetailsDTO(Long id, String nome, Boolean active) {
    public PaymentMethodDetailsDTO(PaymentMethod pM) {
        this(pM.getId(), pM.getName(),  pM.getActive());
    }
}
