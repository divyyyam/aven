package com.divyam.aven.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    private UUID id;

    @Column(name = "owner_ref", nullable = false, updatable = false)
    private String ownerRef;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Account() {
    }

    public Account(String ownerRef) {
        this.id = UUID.randomUUID();
        this.ownerRef = ownerRef;
        this.status = AccountStatus.ACTIVE;
        this.createdAt = Instant.now();
    }

    public void changeStatus(AccountStatus status) {
        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException("A closed account cannot change status");
        }
        this.status = status;
    }

    public UUID getId() { return id; }
    public String getOwnerRef() { return ownerRef; }
    public AccountStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
