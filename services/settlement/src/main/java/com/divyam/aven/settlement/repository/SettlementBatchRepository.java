package com.divyam.aven.settlement.repository;

import com.divyam.aven.settlement.domain.SettlementBatch;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementBatchRepository extends JpaRepository<SettlementBatch, UUID> {
}
