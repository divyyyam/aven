package com.divyam.aven.account.repository;

import com.divyam.aven.account.domain.AccountBalance;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, UUID> {
}
