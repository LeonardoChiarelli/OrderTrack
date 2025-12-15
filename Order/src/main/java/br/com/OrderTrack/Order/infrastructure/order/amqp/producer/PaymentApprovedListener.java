package br.com.OrderTrack.Order.infrastructure.order.amqp.producer;

import br.com.OrderTrack.Order.application.useCase.HandlePaymentApprovedUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentApprovedListener {
    private HandlePaymentApprovedUseCase useCase;

    @RabbitListener(queues = "order.payment.approved")
    public void handle(PaymentApprovedEvent event) {
        useCase.execute(event);
    }

}
