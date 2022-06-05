package com.joaovictor.wishlist.service;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.repository.ProductRepository;
import com.joaovictor.wishlist.factory.ObjectsFactory;
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
    void findProductByIdTest() {
        Product product = ObjectsFactory.getProduct();

        BDDMockito.given(this.productRepository.findById(BDDMockito.anyString())).willReturn(Optional.of(product));

        Product product1 = this.productService.findProductById(BDDMockito.anyString());

        Assertions.assertThat(product1.getId()).isEqualTo(product.getId());
        Assertions.assertThat(product1.getDescription()).isEqualTo(product.getDescription());
        Assertions.assertThat(product1.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    @DisplayName("Should return an exception for productId not found.")
    void notFindProductByIdTest() {
        String messageException = "Product not found.";
        BDDMockito.given(this.productRepository.findById(BDDMockito.anyString())).willReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> this.productService.findProductById(BDDMockito.anyString()));

        Assertions.assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage(messageException);
    }

}