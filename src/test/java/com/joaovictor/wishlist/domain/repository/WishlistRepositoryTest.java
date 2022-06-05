package com.joaovictor.wishlist.domain.repository;

import com.joaovictor.wishlist.domain.entity.Wishlist;
import com.joaovictor.wishlist.factory.ObjectsFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    void findByCustomerIdTest() {
        Wishlist wishlist = ObjectsFactory.getWishlist();

        mongoTemplate.save(wishlist, "wishlist");

        Optional<Wishlist> wishlistOptional = this.wishlistRepository.findByCustomerId(wishlist.getCustomerId());

        Assertions.assertThat(wishlistOptional.isPresent()).isTrue();

    }
}