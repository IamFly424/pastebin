package com.iamfly.hashgenerator.repository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DatabaseInit implements CommandLineRunner {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public DatabaseInit(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }


    @Override
    public void run(String... args) {
        String schemaSql = "DROP TABLE IF EXISTS t_hash; " +
                "CREATE TABLE t_hash (" +
                "id SERIAL PRIMARY KEY," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); " +
                "INSERT INTO t_hash(id)\n" +
                "VALUES (NEXTVAL('t_hash_id_seq'));";
        Mono<Void> monoSchema = r2dbcEntityTemplate.getDatabaseClient().sql(schemaSql).fetch().rowsUpdated().then();
        try {
            monoSchema.block();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
