package com.iamfly.apiservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue apiToHashQueue() {
        return new Queue("api-to-hash");
    }

    @Bean
    public TopicExchange hashidExchange() {
        return new TopicExchange("hashid_exchange");
    }

    @Bean
    public Binding apiToHashBinding(Queue apiToHashQueue, TopicExchange hashidExchange) {
        return BindingBuilder.bind(apiToHashQueue).to(hashidExchange).with("api-to-hash");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public CommandLineRunner declareRabbitResources(AmqpAdmin amqpAdmin,
                                                    Queue queue,
                                                    TopicExchange exchange,
                                                    Binding binding) {
        return args -> {
            amqpAdmin.declareQueue(queue);
            amqpAdmin.declareExchange(exchange);
            amqpAdmin.declareBinding(binding);
        };
    }
}
