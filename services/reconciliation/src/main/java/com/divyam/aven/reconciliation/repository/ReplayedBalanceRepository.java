package com.divyam.aven.reconciliation.repository;
import com.divyam.aven.reconciliation.domain.ReplayedBalance; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface ReplayedBalanceRepository extends JpaRepository<ReplayedBalance, UUID> { }
