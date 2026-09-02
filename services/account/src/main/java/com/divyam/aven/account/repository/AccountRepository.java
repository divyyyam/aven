package com.divyam.aven.account.repository;

import com.divyam.aven.account.domain.Account;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
