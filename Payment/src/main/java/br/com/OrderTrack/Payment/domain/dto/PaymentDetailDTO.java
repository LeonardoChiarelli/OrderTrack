package br.com.OrderTrack.Payment.domain.dto;

import br.com.OrderTrack.Payment.domain.model.Order;
import br.com.OrderTrack.Payment.domain.model.Payment;
import br.com.OrderTrack.Payment.domain.model.PaymentMethod;
import br.com.OrderTrack.Payment.domain.model.PaymentStatus;

import java.math.BigDecimal;

public record PaymentDetailDTO(Long id, BigDecimal totalPrince, PaymentStatus status, Order order, PaymentMethod paymentMethod) {
    public PaymentDetailDTO(Payment p){
        this(p.getId(), p.getTotalPrice(), p.getStatus(), p.getOrder(), p.getPaymentMethod());
    }
}
