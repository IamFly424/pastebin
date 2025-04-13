package com.iamfly.apiservice.models;


import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class UrlRef {

    @Id
    private Integer id;

    private final String url;

    private final LocalDateTime createdAt = LocalDateTime.now();

    public UrlRef(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
