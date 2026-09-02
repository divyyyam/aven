package com.divyam.aven.settlement.api;

import com.divyam.aven.settlement.repository.SettlementItemRepository;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settlements")
public class SettlementController {
    private final SettlementItemRepository items;

    public SettlementController(SettlementItemRepository items) {
        this.items = items;
    }

    @GetMapping("/{transactionId}")
    public SettlementItemResponse find(@PathVariable UUID transactionId) {
        return items.findByTransactionId(transactionId)
                .map(SettlementItemResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Settlement not found: " + transactionId));
    }
}
