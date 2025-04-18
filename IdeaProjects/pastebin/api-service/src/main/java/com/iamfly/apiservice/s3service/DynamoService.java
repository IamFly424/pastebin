package com.iamfly.apiservice.s3service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class DynamoService {

    @Value("${aws.table-name}") private String tableName;
    private final DynamoDbAsyncClient dynamoDbAsyncClient;


    public DynamoService(DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    public Mono<Void> put(String key, String value) {
        return Mono.fromCallable(() -> {
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("object_hash", AttributeValue.fromS(key));
            item.put("delete_at", AttributeValue.fromS(value));
            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(tableName)
                    .item(item)
                    .build();
            return Mono.fromFuture(dynamoDbAsyncClient.putItem(putItemRequest));
        }).then();
    }

}
