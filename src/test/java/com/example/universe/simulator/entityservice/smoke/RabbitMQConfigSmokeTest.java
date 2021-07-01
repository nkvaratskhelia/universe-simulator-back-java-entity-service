package com.example.universe.simulator.entityservice.smoke;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitMQConfigSmokeTest extends AbstractSmokeTest {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private TopicExchange topicExchange;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchangeName;

    @Test
    void test() {
        // -----------------------------------messageConverter-----------------------------------

        // then
        assertThat(messageConverter)
            .isNotNull()
            .isInstanceOf(Jackson2JsonMessageConverter.class);

        // -----------------------------------topicExchange-----------------------------------

        // then
        assertThat(topicExchange).isNotNull();
        assertThat(topicExchange.getName()).isEqualTo(exchangeName);
    }
}
