package br.com.OrderTrack.Payment.infrastructure.persistence.mapper;

import br.com.OrderTrack.Payment.domain.model.Payment;
import br.com.OrderTrack.Payment.domain.model.PaymentMethod;
import br.com.OrderTrack.Payment.infrastructure.persistence.entity.PaymentEntity;
import br.com.OrderTrack.Payment.infrastructure.persistence.entity.PaymentMethodEntity;

public class PaymentEntityMapper {

    public Payment toDomain(PaymentEntity paymentEntity) {
        return Payment.builder()
                .id(paymentEntity.getId())
                .orderId(paymentEntity.getOrderId())
                .amount(paymentEntity.getAmount())
                .paymentMethod(PaymentMethod.builder()
                        .id(paymentEntity.getPaymentMethodId().getId())
                        .name(paymentEntity.getPaymentMethodId().getName())
                        .active(paymentEntity.getPaymentMethodId().isActive())
                        .build())
                .paymentDetails(paymentEntity.getPaymentDetails())
                .build();

    }

    public PaymentEntity toEntity(Payment payment) {
        return new PaymentEntity(payment, new PaymentMethodEntity(payment.getPaymentMethod()));
    }
}
