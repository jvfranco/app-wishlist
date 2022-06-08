package com.joaovictor.wishlist.config;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Configuration
public class Instantiation implements CommandLineRunner {

    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        this.productRepository.deleteAll();

        Product product01 = Product.builder().id("1").description("Tenis Nike").price(BigDecimal.valueOf(100)).build();
        Product product02 = Product.builder().id("2").description("Tenis Adidas").price(BigDecimal.valueOf(150)).build();
        Product product03 = Product.builder().id("3").description("Tenis Reebok").price(BigDecimal.valueOf(200)).build();

        this.productRepository.saveAll(List.of(product01, product02, product03));
    }
}
