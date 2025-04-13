package com.iamfly.hashgenerator.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;

import java.time.Duration;

@Configuration
public class PostgresConfig {

    @Bean
    public ConnectionPool connectionFactory() {
        PostgresqlConnectionConfiguration postgresqlConnectionConfiguration = PostgresqlConnectionConfiguration.builder()
                .port(5432)
                .host("ms_postgres_hash")
                .database("hashid_db")
                .username("iamfly")
                .password("iamfly")
                .build();

        PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(postgresqlConnectionConfiguration);

        ConnectionPoolConfiguration connectionPoolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxSize(20)
                .initialSize(5)
                .maxIdleTime(Duration.ofMinutes(10))
                .maxLifeTime(Duration.ofMinutes(30))
                .build();

        return new ConnectionPool(connectionPoolConfiguration);
    }

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionPool connectionPool) {
        return new R2dbcEntityTemplate(connectionPool);
    }

    @Bean
    public R2dbcTransactionManager r2dbcTransactionManager(ConnectionPool connectionPool) {
        return new R2dbcTransactionManager(connectionPool);
    }

}
