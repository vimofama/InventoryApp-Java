package com.vimofama.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateInventoryDTO(
        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,
        @NotNull(message = "La cantidad inicial es obligatoria")
        @PositiveOrZero(message = "La cantidad inicial debe ser mayor o igual a 0")
        int quantity
) {
}
