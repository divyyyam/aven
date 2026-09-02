package com.divyam.aven.reconciliation.repository;
import com.divyam.aven.reconciliation.domain.ReconciliationOutboxEvent; import java.util.*; import org.springframework.data.domain.Pageable; import org.springframework.data.jpa.repository.JpaRepository;
public interface ReconciliationOutboxRepository extends JpaRepository<ReconciliationOutboxEvent, UUID> { List<ReconciliationOutboxEvent> findByPublishedAtIsNullOrderByCreatedAt(Pageable pageable); }
