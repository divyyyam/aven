package com.divyam.aven.reconciliation.api;

import com.divyam.aven.reconciliation.application.ReconciliationScheduler;
import com.divyam.aven.reconciliation.repository.ReconciliationDriftRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reconciliation")
public class ReconciliationController {
    private final ReconciliationDriftRepository drifts;
    private final ReconciliationScheduler scheduler;

    public ReconciliationController(ReconciliationDriftRepository drifts, ReconciliationScheduler scheduler) {
        this.drifts = drifts;
        this.scheduler = scheduler;
    }

    @GetMapping("/drifts")
    public List<ReconciliationDriftResponse> drifts() {
        return drifts.findAll().stream().map(ReconciliationDriftResponse::from).toList();
    }

    @PostMapping("/run")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void run() {
        scheduler.reconcile();
    }
}
