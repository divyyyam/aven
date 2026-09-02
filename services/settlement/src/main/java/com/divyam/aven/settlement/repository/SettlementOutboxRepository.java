package com.divyam.aven.settlement.repository;

import com.divyam.aven.settlement.domain.SettlementOutboxEvent;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementOutboxRepository extends JpaRepository<SettlementOutboxEvent, UUID> {
    List<SettlementOutboxEvent> findByPublishedAtIsNullOrderByCreatedAt(Pageable pageable);
}
