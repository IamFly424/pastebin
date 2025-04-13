package com.iamfly.apiservice.repo;

import com.iamfly.apiservice.models.UrlRef;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UrlRepo extends R2dbcRepository<UrlRef, Integer> {
}
