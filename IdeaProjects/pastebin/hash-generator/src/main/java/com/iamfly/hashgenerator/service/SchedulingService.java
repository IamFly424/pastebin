package com.iamfly.hashgenerator.service;

import com.iamfly.hashgenerator.assets.Base64generator;
import com.iamfly.hashgenerator.assets.AtomicIntegerHolder;
import com.iamfly.hashgenerator.model.Hash;
import com.iamfly.hashgenerator.repository.HashRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class SchedulingService {

    private final ReactiveRedisTemplate<Integer, String> reactiveRedisTemplate;
    private final HashRepository hashRepository;
    private final AtomicIntegerHolder atomicIntegerHolder;


    public SchedulingService(@Qualifier("hashReactiveRedisTemplate") ReactiveRedisTemplate<Integer, String> reactiveRedisTemplate, HashRepository hashRepository, AtomicIntegerHolder atomicIntegerHolder) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.hashRepository = hashRepository;
        this.atomicIntegerHolder = atomicIntegerHolder;
    }

    @Scheduled(fixedRate = 640000)
    public Mono<Void> schedule() {
        return hashRepository.getNextHashId()
                .flatMap(hashId -> Flux.range(hashId, 1000)
                        .flatMap(i ->
                                Base64generator.generateBase64(i)
                                        .flatMap(hash ->
                                                hashRepository.returnId()
                                                        .flatMap(e -> reactiveRedisTemplate.opsForValue().set(i - 1, hash))
                                        )
                        ).then()
                )
                .then()
                .doOnTerminate(() -> System.out.println("Finished processing hashes!"));
    }
}
