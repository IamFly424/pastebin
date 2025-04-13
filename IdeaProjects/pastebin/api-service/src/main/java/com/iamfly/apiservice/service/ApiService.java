package com.iamfly.apiservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ApiService {


    private final RabbitTemplate rabbitTemplate;

    public ApiService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Void> createPost() {
        return Mono.fromRunnable(() -> rabbitTemplate.convertAndSend("api-to-hash", ""))
                .doOnTerminate(() -> rabbitTemplate.receive("hash-to-api", 2000))
                .map(e -> "ss").then();


    }

}
