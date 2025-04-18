package com.iamfly.apiservice.s3service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.BytesWrapper;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Service
public class S3Service {

    private final S3AsyncClient s3Client;

    @Value("${aws.bucket-name}") private String bucketName;

    public S3Service(S3AsyncClient s3Client) {
        this.s3Client = s3Client;
    }


    public Mono<Void> putObjectFile(String key, AsyncRequestBody requestBody) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("text/plain")
                .contentLength(requestBody.contentLength().isPresent() ? requestBody.contentLength().get() : 0)
                .build();

        return Mono.fromFuture(s3Client.putObject(putRequest, requestBody))
                .then();
    }

    public Mono<byte[]> getObject(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key("7days/" + key)
                .build();


        return Mono.fromFuture(s3Client.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
                .thenApply(BytesWrapper::asByteArray));

    }



}
