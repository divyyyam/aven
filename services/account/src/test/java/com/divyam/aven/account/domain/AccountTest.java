package com.divyam.aven.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AccountTest {
    @Test
    void newAccountIsActiveAndCanBeFrozen() {
        Account account = new Account("owner-1");
        account.changeStatus(AccountStatus.FROZEN);
        assertThat(account.getStatus()).isEqualTo(AccountStatus.FROZEN);
    }

    @Test
    void closedAccountCannotBeReopened() {
        Account account = new Account("owner-1");
        account.changeStatus(AccountStatus.CLOSED);
        assertThatThrownBy(() -> account.changeStatus(AccountStatus.ACTIVE))
                .isInstanceOf(IllegalStateException.class);
    }
}
