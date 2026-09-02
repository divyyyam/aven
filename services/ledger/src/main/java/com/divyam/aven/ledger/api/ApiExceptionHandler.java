package com.divyam.aven.ledger.api;

import com.divyam.aven.ledger.domain.LedgerValidationException;
import com.divyam.aven.ledger.domain.ReversalNotAllowedException;
import com.divyam.aven.ledger.domain.TransactionNotFoundException;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(LedgerValidationException.class)
    ProblemDetail handleValidation(LedgerValidationException exception) {
        return problem(HttpStatus.UNPROCESSABLE_CONTENT, "Unbalanced transaction", exception.getMessage());
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    ProblemDetail handleNotFound(TransactionNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "Transaction not found", exception.getMessage());
    }

    @ExceptionHandler(ReversalNotAllowedException.class)
    ProblemDetail handleReversalConflict(ReversalNotAllowedException exception) {
        return problem(HttpStatus.CONFLICT, "Reversal not allowed", exception.getMessage());
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create("https://aven.local/problems/" + status.value()));
        return problem;
    }
}
