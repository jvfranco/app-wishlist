package com.joaovictor.wishlist.service;

import com.joaovictor.wishlist.domain.entity.Wishlist;
import com.joaovictor.wishlist.domain.repository.WishlistRepository;
import com.joaovictor.wishlist.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WishlistService {

    private final String messageMaxProducts = "Wishlist with more than %d products.";
    private final String messageNoProducts = "Wishlist is empty.";
    private final String messageProductNotExist = "Product does not exist in this wishlist";

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    public Wishlist getAllProductsByCustomerId(String customerId) throws BusinessException {
        return findWishlistByCustomerId(customerId);
    }

    public Wishlist addProductToWishlist(String customerId, String productId) throws BusinessException {
        var wishlist = this.findWishlistByCustomerId(customerId);
        var product = this.productService.findProductById(productId);

        boolean wasAdded = wishlist.addProductToList(product);

        if (!wasAdded) {
            throw new BusinessException(String.format(messageMaxProducts, 20));
        }

        return wishlist;
    }

    public Wishlist removeProductToWishlist(String customerId, String productId) throws BusinessException {
        var wishlist = this.findWishlistByCustomerId(customerId);

        if (wishlist.getProducts() != null && !wishlist.getProducts().isEmpty()) {
            var product = this.productService.findProductById(productId);
            if (wishlist.getProducts().contains(product)) {
                wishlist.removeProductToList(product);
                return wishlist;
            } else {
                throw new BusinessException(messageProductNotExist);
            }
        } else {
            throw new BusinessException(messageNoProducts);
        }
    }

    public boolean consultProductInWishList(String customerId, String productId) throws BusinessException {
        var wishlist = this.findWishlistByCustomerId(customerId);

        return wishlist.getProducts().stream()
                .anyMatch(p -> p.getId().equalsIgnoreCase(productId));
    }

    public Wishlist findWishlistByCustomerId(String customerId) throws BusinessException {
        return this.wishlistRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("Wishlist not found."));
    }
}
