package com.divyam.aven.account.application;

import com.divyam.aven.account.domain.Account;
import com.divyam.aven.account.domain.AccountBalance;
import com.divyam.aven.account.domain.AccountStatus;
import com.divyam.aven.account.repository.AccountBalanceRepository;
import com.divyam.aven.account.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AccountRepository accounts;
    private final AccountBalanceRepository balances;

    public AccountService(AccountRepository accounts, AccountBalanceRepository balances) {
        this.accounts = accounts;
        this.balances = balances;
    }

    @Transactional
    public Account create(String ownerRef) {
        Account account = accounts.save(new Account(ownerRef));
        balances.save(new AccountBalance(account.getId()));
        return account;
    }

    @Transactional
    public Account changeStatus(UUID accountId, AccountStatus status) {
        Account account = accounts.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.changeStatus(status);
        return account;
    }

    @Transactional(readOnly = true)
    public AccountBalance balance(UUID accountId) {
        return balances.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Transactional
    public AccountBalance forceBalanceForDemo(UUID accountId, BigDecimal value) {
        AccountBalance balance = balances.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        balance.forceBalanceForDemo(value);
        return balance;
    }
}
