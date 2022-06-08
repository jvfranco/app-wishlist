package com.joaovictor.wishlist.service;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.entity.Wishlist;
import com.joaovictor.wishlist.domain.repository.WishlistRepository;
import com.joaovictor.wishlist.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WishlistService {

    private final static String messageMaxProducts = "Wishlist with more than %d products.";
    private final static String messageNoProducts = "Wishlist is empty.";
    private final static String messageProductNotExist = "Product does not exist in this wishlist";

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    public Wishlist getAllProductsByCustomerId(String customerId) {
        return findWishlistByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException(messageNoProducts));
    }

    public Wishlist addProductToWishlist(String customerId, String productId) {
        var wishlist = this.findWishlistByCustomerId(customerId)
                .orElse(Wishlist.builder().customerId(customerId).build());

        var product = this.productService.findProductById(productId);

        boolean wasAdded = wishlist.addProductToList(product);

        if (!wasAdded) {
            throw new BusinessException(String.format(messageMaxProducts, 20));
        }

        return this.wishlistRepository.save(wishlist);
    }

    public Wishlist removeProductToWishlist(String customerId, String productId) {
        var wishlist = this.findWishlistByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException(messageNoProducts));

        if (wishlist.getProducts() != null && !wishlist.getProducts().isEmpty()) {
            wishlist.removeProductToList(productId);
            return this.wishlistRepository.save(wishlist);
        }
        throw new BusinessException(messageNoProducts);
    }

    public boolean checkProductInWishList(String customerId, String productId) {
        var wishlist = this.findWishlistByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException(messageNoProducts));

        return wishlist.getProducts().stream()
                .anyMatch(p -> p.getId().equalsIgnoreCase(productId));
    }

    public Optional<Wishlist> findWishlistByCustomerId(String customerId) {
        return this.wishlistRepository.findByCustomerId(customerId);
    }
}
