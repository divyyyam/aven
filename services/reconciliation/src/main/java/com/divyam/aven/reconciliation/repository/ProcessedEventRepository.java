package com.divyam.aven.reconciliation.repository;
import com.divyam.aven.reconciliation.domain.ProcessedEvent; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, UUID> { }
