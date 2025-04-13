package com.iamfly.hashgenerator.assets;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;


@Component
public class AtomicIntegerHolder {

    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    public AtomicInteger get() {
        return this.atomicInteger;
    }
}
