package br.com.OrderTrack.Order.infrastructure.messaging.listener;

import br.com.OrderTrack.Order.infrastructure.configuration.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentDlqListener {

    @RabbitListener(queues = RabbitConfig.PAYMENT_APPROVED_DLQ)
    public void processPaymentDlq(Message message){
        String body = new String(message.getBody());

        log.error("CRITICAL: Mensagem caiu na DLQ (Dead Letter Queue). Conteúdo: {}", body);
        log.error("Headers: {}", message.getMessageProperties().getHeaders());
    }
}
