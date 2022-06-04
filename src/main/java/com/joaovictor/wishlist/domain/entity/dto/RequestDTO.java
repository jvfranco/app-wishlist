package com.joaovictor.wishlist.domain.entity.dto;

import javax.validation.constraints.NotNull;

public record RequestDTO(
        @NotNull(message = "customerId not be null") String customerId,
        @NotNull(message = "productId not be null") String productId) {
}
