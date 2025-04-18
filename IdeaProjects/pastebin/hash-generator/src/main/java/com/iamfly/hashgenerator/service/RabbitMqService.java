package com.iamfly.hashgenerator.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;
    private final HashService hashService;

    public RabbitMqService(RabbitTemplate rabbitTemplate, HashService hashService) {
        this.rabbitTemplate = rabbitTemplate;
        this.hashService = hashService;
    }


    @RabbitListener(queues = "api-to-hash")
    public Mono<Void> receive(String message) {
        return hashService.getHash()
                        .doOnNext(e -> rabbitTemplate.convertAndSend("hash-to-api", e))
                .then();
    }
}
