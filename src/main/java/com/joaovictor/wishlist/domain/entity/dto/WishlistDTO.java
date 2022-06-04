package com.joaovictor.wishlist.domain.entity.dto;

import com.joaovictor.wishlist.domain.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public record WishlistDTO(String customerId, List<Product> products, BigDecimal amount) {
}
