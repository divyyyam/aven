package com.divyam.aven.account.api;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(@NotBlank String ownerRef) {
}
