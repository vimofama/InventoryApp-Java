package com.vimofama.productservice.dto;

import java.math.BigDecimal;

public record CreateProductDTO(
        String name,
        String description,
        BigDecimal price,
        String sku
) {
}
