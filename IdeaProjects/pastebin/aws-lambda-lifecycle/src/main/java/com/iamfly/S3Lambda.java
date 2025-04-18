package com.iamfly;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class S3Lambda implements RequestHandler<Object, String> {

    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();
    private final S3Client s3Client = S3Client.create();

    private static final String BUCKET = "aws-docx-bucket";
    private static final String TABLE = "aws_dynamodb_testik";

    @Override
    public String handleRequest(Object o, Context context) {
        System.out.println( "Hello World! from AWS Lambda");
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE)
                .build();
        List<Map<String, AttributeValue>> items = dynamoDbClient.scan(scanRequest).items();
        Instant instant = Instant.now();

        for (Map<String, AttributeValue> item : items) {
            String key = item.get("object_hash").s();
            String value = item.get("delete_at").s();

            Instant deleteAt = Instant.parse(value);

            if (instant.isAfter(deleteAt)) {
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(BUCKET)
                        .key(key).build()
                );

                dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                        .tableName(TABLE)
                        .key(Map.of("object_hash", AttributeValue.fromS(key)))
                        .build()
                );
                context.getLogger().log("Deleted " + key + " from bucket " + BUCKET);
            }
        }
        return "success";
    }
}
