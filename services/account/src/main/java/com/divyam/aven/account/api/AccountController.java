package com.divyam.aven.account.api;

import com.divyam.aven.account.application.AccountService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AccountResponse.from(accountService.create(request.ownerRef())));
    }

    @PatchMapping("/{accountId}/status")
    public AccountResponse changeStatus(
            @PathVariable UUID accountId,
            @Valid @RequestBody UpdateAccountStatusRequest request) {
        return AccountResponse.from(accountService.changeStatus(accountId, request.status()));
    }

    @GetMapping("/{accountId}/balance")
    public BalanceResponse balance(@PathVariable UUID accountId) {
        return BalanceResponse.from(accountService.balance(accountId));
    }
}
