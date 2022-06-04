package com.joaovictor.wishlist.service;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product findProductById(String productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found."));

        return product;
    }
}
