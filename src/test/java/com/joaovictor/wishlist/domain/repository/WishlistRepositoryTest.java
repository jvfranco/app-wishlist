package com.joaovictor.wishlist.domain.repository;

import com.joaovictor.wishlist.domain.entity.Wishlist;
import com.joaovictor.wishlist.factory.ObjectsFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class WishlistRepositoryTest {

    @MockBean
    WishlistRepository wishlistRepository;

    @Test
    @DisplayName("Must return a wishlist by customerId.")
    void findByCustomerIdTest() {
        Wishlist wishlist = ObjectsFactory.getWishlist();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));

        Optional<Wishlist> wishlistTest = this.wishlistRepository.findByCustomerId(BDDMockito.anyString());

        Assertions.assertThat(wishlistTest.isPresent()).isTrue();
    }


}