package com.iamfly.apiservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class S3Config {

    @Value("${aws.secret-key}")
    private String secretKey;

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.bucket-name}") private String bucketName;



    @Bean
    public S3AsyncClient s3AsyncClient() {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3AsyncClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

}
