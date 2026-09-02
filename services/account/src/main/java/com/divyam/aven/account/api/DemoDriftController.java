package com.divyam.aven.account.api;

import com.divyam.aven.account.application.AccountService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/accounts")
@Profile("demo")
public class DemoDriftController {
    private final AccountService accountService;

    public DemoDriftController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/force-balance")
    public BalanceResponse forceBalance(
            @PathVariable UUID accountId,
            @Valid @RequestBody DemoBalanceRequest request) {
        return BalanceResponse.from(accountService.forceBalanceForDemo(accountId, request.balance()));
    }
}
