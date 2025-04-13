package com.iamfly.hashgenerator.assets;

import reactor.core.publisher.Mono;

import java.util.Base64;

public abstract class Base64generator {

    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static Mono<String> generateBase64(int id) {
        return generateBytes(id)
                .flatMap(Base64generator::base64Encode);
    }

    public static Mono<byte[]> generateBytes(int id) {
        return Mono.defer(() -> Mono.just(new byte[]{
                (byte) (id >> 24),
                (byte) (id >> 16),
                (byte) (id >> 8),
                (byte) (id)
        }));
    }

    public static Mono<String> base64Encode(byte[] bytes) {
        return Mono.defer(() -> Mono.just(encoder.encodeToString(bytes)));
    }

}
