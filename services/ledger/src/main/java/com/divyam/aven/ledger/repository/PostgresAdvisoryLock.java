package com.divyam.aven.ledger.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresAdvisoryLock {
    private final EntityManager entityManager;

    public PostgresAdvisoryLock(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void lock(String key) {
        entityManager.createNativeQuery("select pg_advisory_xact_lock(hashtextextended(:key, 0))")
                .setParameter("key", key)
                .getSingleResult();
    }
}
