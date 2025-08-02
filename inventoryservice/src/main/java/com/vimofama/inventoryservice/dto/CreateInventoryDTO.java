package com.vimofama.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreateInventoryDTO(
        @NotNull
        Long productId,
        @NotNull
        int quantity
) {
}
