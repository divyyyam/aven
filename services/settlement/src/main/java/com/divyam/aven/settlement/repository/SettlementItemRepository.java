package com.divyam.aven.settlement.repository;

import com.divyam.aven.settlement.domain.SettlementItem;
import com.divyam.aven.settlement.domain.SettlementStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementItemRepository extends JpaRepository<SettlementItem, UUID> {
    Optional<SettlementItem> findByTransactionId(UUID transactionId);
    List<SettlementItem> findByStatusInAndNextAttemptAtLessThanEqualOrderByNextAttemptAt(
            List<SettlementStatus> statuses, Instant now, Pageable pageable);
}
