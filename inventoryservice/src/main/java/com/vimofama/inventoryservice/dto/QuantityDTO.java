package com.vimofama.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QuantityDTO(
        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser un número positivo mayor a 0")
        int quantity
) {
}
