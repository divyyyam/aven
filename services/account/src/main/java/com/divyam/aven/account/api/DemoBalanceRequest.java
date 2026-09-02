package com.divyam.aven.account.api;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record DemoBalanceRequest(@NotNull BigDecimal balance) {
}
