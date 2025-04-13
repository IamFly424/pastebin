package com.iamfly.hashgenerator.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("t_hash")
public class Hash {

    @Id
    private Integer id;

    @Column("created_at")
    private final LocalDateTime createdAt;

    public Hash(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
