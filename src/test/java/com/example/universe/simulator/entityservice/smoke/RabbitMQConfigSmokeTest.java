package com.example.universe.simulator.entityservice.smoke;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMQConfigSmokeTest extends AbstractSmokeTest {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private Declarables declarables;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.event-queue}")
    private String eventQueueName;

    @Test
    void test() {
        // messageConverter
        assertThat(messageConverter)
            .isNotNull()
            .isInstanceOf(Jackson2JsonMessageConverter.class);

        // declarables

        assertThat(declarables).isNotNull();

        // exchanges
        List<Exchange> exchanges = declarables.getDeclarablesByType(Exchange.class);
        assertThat(exchanges).hasSize(1);
        assertThat(exchanges.get(0)).isInstanceOf(DirectExchange.class);
        assertThat(exchanges.get(0).getName()).isEqualTo(exchangeName);

        // queues
        List<Queue> queues = declarables.getDeclarablesByType(Queue.class);
        assertThat(queues).hasSize(1);
        assertThat(queues.get(0).getName()).isEqualTo(eventQueueName);

        // bindings
        List<Binding> bindings = declarables.getDeclarablesByType(Binding.class);
        assertThat(bindings).hasSize(1);
        assertThat(bindings.get(0).getExchange()).isEqualTo(exchangeName);
        assertThat(bindings.get(0).getDestination()).isEqualTo(eventQueueName);
        assertThat(bindings.get(0).getRoutingKey()).isEqualTo(eventQueueName);
    }
}
