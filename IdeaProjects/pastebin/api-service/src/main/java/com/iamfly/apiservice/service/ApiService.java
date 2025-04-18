package com.iamfly.apiservice.service;

import com.iamfly.apiservice.s3service.DynamoService;
import com.iamfly.apiservice.s3service.S3Service;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;


@Service
public class ApiService {


    private final RabbitTemplate rabbitTemplate;
    private final S3Service s3Service;
    private final DynamoService dynamoService;

    public ApiService(RabbitTemplate rabbitTemplate, S3Service s3Service, DynamoService dynamoService) {
        this.rabbitTemplate = rabbitTemplate;
        this.s3Service = s3Service;
        this.dynamoService = dynamoService;
    }


    public Mono<String> getPost(String hash) {
        return s3Service.getObject(hash)
                .map(e -> new String(e, StandardCharsets.UTF_8));
    }

    public Mono<Void> createPost(Instant expireTime, FilePart filePart) {
        Mono<String> hashMono = Mono.fromCallable(() -> {
            rabbitTemplate.convertAndSend("api-to-hash", "");
            Message message = rabbitTemplate.receive("hash-to-api", 5000);
            return message != null ? new String(message.getBody(), StandardCharsets.UTF_8) : null;
        }).subscribeOn(Schedulers.boundedElastic());

        Mono<byte[]> bytesMono = DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                }).subscribeOn(Schedulers.boundedElastic());

        return Mono.zip(hashMono, bytesMono)
                .flatMap(tuple -> {
                    String hash = tuple.getT1();
                    byte[] bytes = tuple.getT2();

                    AsyncRequestBody body = AsyncRequestBody.fromBytes(bytes);

                    return s3Service.putObjectFile(hash, body)
                            .then(dynamoService.put(hash, expireTime.toString()));
                });
    }



}
