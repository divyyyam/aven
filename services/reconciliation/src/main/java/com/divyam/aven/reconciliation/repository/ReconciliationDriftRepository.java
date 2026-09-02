package com.divyam.aven.reconciliation.repository;
import com.divyam.aven.reconciliation.domain.ReconciliationDrift; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface ReconciliationDriftRepository extends JpaRepository<ReconciliationDrift, UUID> { Optional<ReconciliationDrift> findByAccountIdAndResolvedAtIsNull(UUID accountId); }
