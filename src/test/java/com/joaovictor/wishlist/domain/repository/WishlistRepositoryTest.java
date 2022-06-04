package com.joaovictor.wishlist.domain.repository;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.entity.Wishlist;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataMongoTest
class WishlistRepositoryTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    WishlistRepository wishlistRepository;

    @Test
    @DisplayName("Must return a wishlist by customerId.")
    public void findByCustomerIdTest() {
        String id = "1";
        String productId = "1";
        String customerId = "1";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description("Product 01").price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).customerId(customerId).products(List.of(product)).amount(value).build();

        mongoTemplate.save(wishlist, "wishlist");

        Optional<Wishlist> wishlistOptional = this.wishlistRepository.findByCustomerId(customerId);

        Assertions.assertThat(wishlistOptional.isPresent()).isTrue();

    }
}