package com.divyam.aven.ledger.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LedgerTransactionTest {

    @Test
    void reversalInvertsEveryEntryAndKeepsOriginalEntriesUnchanged() {
        LedgerTransaction original = LedgerTransaction.create("original-key");
        UUID debitAccount = UUID.randomUUID();
        UUID creditAccount = UUID.randomUUID();
        original.addEntry(debitAccount, new BigDecimal("10.0000"), EntryDirection.DEBIT);
        original.addEntry(creditAccount, new BigDecimal("10.0000"), EntryDirection.CREDIT);

        LedgerTransaction reversal = LedgerTransaction.reversalOf("reversal-key", original.getId());
        original.getEntries().forEach(entry -> reversal.addEntry(
                entry.getAccountId(), entry.getAmount(), entry.getDirection().inverse()));
        original.markReversedBy(reversal.getId());

        assertThat(original.getEntries()).extracting(LedgerEntry::getDirection)
                .containsExactly(EntryDirection.DEBIT, EntryDirection.CREDIT);
        assertThat(reversal.getReversedTransactionId()).isEqualTo(original.getId());
        assertThat(reversal.getEntries()).extracting(LedgerEntry::getDirection)
                .containsExactly(EntryDirection.CREDIT, EntryDirection.DEBIT);
    }

    @Test
    void transactionCannotBeReversedTwice() {
        LedgerTransaction transaction = LedgerTransaction.create("key");
        transaction.markReversedBy(UUID.randomUUID());

        assertThatThrownBy(() -> transaction.markReversedBy(UUID.randomUUID()))
                .isInstanceOf(ReversalNotAllowedException.class);
    }

    @Test
    void debitAndCreditHaveOppositeSignedAmounts() {
        BigDecimal amount = new BigDecimal("42.2500");

        assertThat(EntryDirection.DEBIT.signedAmount(amount)).isEqualByComparingTo("42.2500");
        assertThat(EntryDirection.CREDIT.signedAmount(amount)).isEqualByComparingTo("-42.2500");
    }
}
