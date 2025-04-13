package com.iamfly.hashgenerator.repository;

import com.iamfly.hashgenerator.model.Hash;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface HashRepository extends R2dbcRepository<Hash, Integer> {

    @Query("SELECT NEXTVAL('t_hash_id_seq')")
    Mono<Integer> getNextHashId();

    @Query("INSERT INTO t_hash(hash) VALUES ($1) RETURNING hash")
    Mono<String> getHash(String hash);

    @Query("INSERT INTO t_hash DEFAULT VALUES RETURNING id")
    Mono<Integer> returnId();

}
