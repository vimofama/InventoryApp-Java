package com.vimofama.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QuantityDTO(
        @NotNull
        @Positive
        int quantity
) {
}
