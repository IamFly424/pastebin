package com.iamfly.hashgenerator.service;

import com.iamfly.hashgenerator.assets.AtomicIntegerHolder;
import com.iamfly.hashgenerator.assets.Base64generator;
import com.iamfly.hashgenerator.model.Hash;
import com.iamfly.hashgenerator.repository.HashRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class HashService {

    private final HashRepository hashRepository;
    private final ReactiveRedisTemplate<Integer, String> reactiveRedisTemplate;
    private final AtomicIntegerHolder atomicIntegerHolder;

    public HashService(HashRepository hashRepository,
                       @Qualifier("hashReactiveRedisTemplate") ReactiveRedisTemplate<Integer, String> reactiveRedisTemplate, AtomicIntegerHolder atomicIntegerHolder) {
        this.hashRepository = hashRepository;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.atomicIntegerHolder = atomicIntegerHolder;
    }

    public Mono<String> getHash() {
        return reactiveRedisTemplate.opsForValue().getAndDelete(atomicIntegerHolder.get().getAndIncrement())
                .switchIfEmpty(Mono.fromCallable(() -> atomicIntegerHolder.get().get() != 1)
                        .flatMap(e -> {
                            if (e) {
                                atomicIntegerHolder.get().set(1);
                            }
                            return Mono.empty();
                        })
                        .flatMap(e -> hashRepository.returnId())
                        .flatMap(Base64generator::generateBase64));
    }





}
