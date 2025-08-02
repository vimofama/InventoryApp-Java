package com.vimofama.productservice.dto;

import java.math.BigDecimal;

public record UpdateProductDTO(
        String name,
        String description,
        BigDecimal price,
        String sku
) {
}
