package com.iamfly.apiservice.s3service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class S3Init implements ApplicationListener<ApplicationReadyEvent> {

    private final S3AsyncClient s3AsyncClient;
    @Value("${aws.bucket-name}") private String bucketName;

    public S3Init(S3AsyncClient s3AsyncClient) {
        this.s3AsyncClient = s3AsyncClient;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<LifecycleRule> rules = Arrays.asList(
                LifecycleRule.builder()
                        .id("ExpireAfter30Days")
                        .filter(LifecycleRuleFilter.builder().prefix("30days/").build())
                        .status(ExpirationStatus.ENABLED)
                        .expiration(LifecycleExpiration.builder().days(30).build())
                        .build(),

                LifecycleRule.builder()
                        .id("ExpireAfter7Days")
                        .filter(LifecycleRuleFilter.builder().prefix("7days/").build())
                        .status(ExpirationStatus.ENABLED)
                        .expiration(LifecycleExpiration.builder().days(7).build())
                        .build(),

                LifecycleRule.builder()
                        .id("ExpireAfter1Days")
                        .filter(LifecycleRuleFilter.builder().prefix("1days/").build())
                        .status(ExpirationStatus.ENABLED)
                        .expiration(LifecycleExpiration.builder().days(1).build())
                        .build()
        );

        BucketLifecycleConfiguration bucketLifecycleConfiguration = BucketLifecycleConfiguration.builder()
                .rules(rules)
                .build();

        PutBucketLifecycleConfigurationRequest bucketLifecycleConfigurationRequest = PutBucketLifecycleConfigurationRequest.builder()
                .bucket(bucketName)
                .lifecycleConfiguration(bucketLifecycleConfiguration)
                .build();

        try {
            s3AsyncClient.putBucketLifecycleConfiguration(bucketLifecycleConfigurationRequest).get(5, TimeUnit.SECONDS);;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
