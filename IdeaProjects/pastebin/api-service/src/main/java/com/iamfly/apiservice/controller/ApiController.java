package com.iamfly.apiservice.controller;


import com.iamfly.apiservice.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }


    @PostMapping("/create")
    public Mono<ResponseEntity<String>> create(@RequestPart("file") FilePart filePart) {
        var now = Instant.now();
        return apiService.createPost(now.plus(Duration.ofDays(1)), filePart)
                .doOnError(throwable -> throwable.printStackTrace())
                .then(Mono.just(ResponseEntity.ok().body("success")));
    }

    @GetMapping("/get")
    public Mono<ResponseEntity<String>> get(@RequestParam("hash") String hash) {
        return apiService.getPost(hash)
                .map(e -> ResponseEntity.ok().body(e));
    }

}
