package com.joaovictor.wishlist.factory;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.entity.Wishlist;

import java.math.BigDecimal;
import java.util.List;

public class ObjectsFactory {

    public static List<Product> getProducts() {
        return List.of(
                Product.builder()
                        .id("1")
                        .description("Product 01")
                        .price(BigDecimal.valueOf(10.0))
                        .build());
    }

    public static Product getProduct() {
        return Product.builder()
                .id("1")
                .description("Product 01")
                .price(BigDecimal.valueOf(10.0))
                .build();
    }

    public static Wishlist getWishlist() {
        return Wishlist.builder()
                .id("1")
                .customerId("1")
                .products(getProducts())
                .amount(BigDecimal.valueOf(10.0))
                .build();
    }

    public static Wishlist getWishlistEmpty() {
        return Wishlist.builder()
                .id("1")
                .customerId("1")
                .build();
    }
}
