package br.com.OrderTrack.Order.infrastructure.configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PAYMENT_APPROVED_QUEUE = "payment.approved.order.queue";
    public static final String PAYMENT_REJECTED_QUEUE = "payment.rejected.order.queue";
    public static final String PAYMENT_APPROVED_DLQ = "payment.approved.dlq";
    public static final String STOCK_RESERVED_QUEUE = "stock.reserved.order.queue";
    public static final String STOCK_FAILED_QUEUE = "stock.failed.order.queue";
    public static final String PRODUCT_EVENTS_QUEUE = "product.events.order.queue";

    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String PAYMENT_EXCHANGE = "payment-exchange";
    public static final String INVENTORY_EXCHANGE = "inventory-exchange";
    public static final String PRODUCT_EXCHANGE = "product-exchange";

    @Bean
    public Queue stockReservedQueue() {
        return QueueBuilder.durable(STOCK_RESERVED_QUEUE).build();
    }

    @Bean
    public Queue stockFailedQueue() {
        return QueueBuilder.durable(STOCK_FAILED_QUEUE).build();
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Binding bindingStockReserved(Queue stockReservedQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(stockReservedQueue).to(inventoryExchange).with("stock.reserved.#");
    }

    @Bean
    public Binding bindingStockFailed(Queue stockFailedQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(stockFailedQueue).to(inventoryExchange).with("stock.failed.#");
    }

    @Bean
    public Queue paymentApprovedDlq() {
        return QueueBuilder.durable(PAYMENT_APPROVED_DLQ).build();
    }

    @Bean
    public Queue paymentApprovedQueue() {
        return QueueBuilder.durable(PAYMENT_APPROVED_QUEUE)
                .withArgument("x-dead-letter-exchange", "") // Default exchange
                .withArgument("x-dead-letter-routing-key", PAYMENT_APPROVED_DLQ)
                .build();
    }

    @Bean
    public Queue paymentRejectedQueue() {
        return QueueBuilder.durable(PAYMENT_REJECTED_QUEUE).build();
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Binding bindingPaymentApproved(Queue paymentApprovedQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentApprovedQueue).to(paymentExchange).with("payment.approved.#");
    }

    @Bean
    public Binding bindingPaymentRejected(Queue paymentRejectedQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentRejectedQueue).to(paymentExchange).with("payment.rejected.#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public Queue productEventsQueue() {
        return QueueBuilder.durable(PRODUCT_EVENTS_QUEUE).build();
    }

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Binding bindingProductEvents(Queue productEventsQueue, TopicExchange productExchange) {
        return BindingBuilder.bind(productEventsQueue).to(productExchange).with("product.#");
    }
}
