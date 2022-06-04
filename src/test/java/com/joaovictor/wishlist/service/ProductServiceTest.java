package com.joaovictor.wishlist.service;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ProductServiceTest {

    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        this.productService = new ProductService(this.productRepository);
    }

    @Test
    @DisplayName("Must return a product by productId.")
    public void findProductByIdTest() {
        String productId = "1";
        String description = "Product 01";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description(description).price(value).build();

        BDDMockito.given(this.productRepository.findById(productId)).willReturn(Optional.of(product));

        Product product1 = this.productService.findProductById(productId);

        Assertions.assertThat(product1.getId()).isEqualTo(productId);
        Assertions.assertThat(product1.getDescription()).isEqualTo(description);
        Assertions.assertThat(product1.getPrice()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should return an exception for productId not found.")
    public void notFindProductByIdTest() {
        String productId = "2";
        String messageException = "Product not found.";
        BDDMockito.given(this.productRepository.findById(productId)).willReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> this.productService.findProductById(productId));

        Assertions.assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage(messageException);
    }

}