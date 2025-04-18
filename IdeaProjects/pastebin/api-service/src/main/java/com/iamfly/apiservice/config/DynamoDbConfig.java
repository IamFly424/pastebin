package com.iamfly.apiservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

import java.time.Instant;

@Configuration
public class DynamoDbConfig {

    @Value("${aws.secret-key}")
    private String secretKey;

    @Value("${aws.access-key}")
    private String accessKey;

    @Bean
    public DynamoDbAsyncClient dynamoDbClient() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return DynamoDbAsyncClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
    

}
