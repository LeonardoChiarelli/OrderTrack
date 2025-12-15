package br.com.OrderTrack.Order.infrastructure.order.messaging.listener;

import br.com.OrderTrack.Order.application.useCase.HandlePaymentApprovedUseCase;
import br.com.OrderTrack.Order.application.useCase.HandlePaymentRejectedUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    @Autowired
    private HandlePaymentApprovedUseCase approvedUseCase;

    @Autowired
    private HandlePaymentRejectedUseCase rejectedUseCase;

    @RabbitListener(queues = "payment.approved.order.queue")
    public void handlePaymentApproved(String payload) {
        // Deserializar payload para pegar o orderId
        // approvedUseCase.execute(orderId);
        System.out.println("Pagamento Aprovado recebido: " + payload);
    }

    @RabbitListener(queues = "payment.rejected.order.queue")
    public void handlePaymentRejected(String payload) {
        // rejectedUseCase.execute(orderId);
        System.out.println("Pagamento Rejeitado recebido: " + payload);
    }
}
