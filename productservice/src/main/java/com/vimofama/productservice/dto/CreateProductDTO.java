package com.vimofama.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductDTO(
        @NotNull(message = "El nombre del producto es obligatorio")
        @NotBlank(message = "El nombre del producto no puede estar vacío")
        @Size(min = 3, message = "El nombre del producto debe tener al menos 3 caracteres")
        String name,

        @NotNull(message = "La descripción del producto es obligatoria")
        @NotBlank(message = "La descripción del producto no puede estar vacía")
        @Size(min = 3, message = "La descripción del producto debe tener al menos 3 caracteres")
        String description,

        @NotNull(message = "El precio del producto es obligatorio")
        @Positive(message = "El precio del producto debe ser mayor a 0")
        BigDecimal price,

        @NotNull(message = "El SKU del producto es obligatorio")
        @NotBlank(message = "El SKU del producto no puede estar vacío")
        @Size(min = 3, message = "El SKU del producto debe tener al menos 3 caracteres")
        String sku
) {
}
