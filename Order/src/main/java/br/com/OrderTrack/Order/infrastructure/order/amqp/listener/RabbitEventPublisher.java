package br.com.OrderTrack.Order.infrastructure.order.amqp.listener;

import br.com.OrderTrack.Order.application.port.out.EventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitEventPublisher implements EventPublisher {
    private RabbitTemplate rabbitTemplate;

    @Override
    public void publish(Object event) {
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.requested",
                event
        );
    }
}
