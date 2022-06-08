package com.joaovictor.wishlist.service;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.entity.Wishlist;
import com.joaovictor.wishlist.domain.repository.WishlistRepository;
import com.joaovictor.wishlist.exception.BusinessException;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class WishlistServiceTest {

    WishlistService wishlistService;

    @MockBean
    WishlistRepository wishlistRepository;

    @MockBean
    ProductService productService;

    @BeforeEach
    public void setUp() {
        this.wishlistService = new WishlistService(this.wishlistRepository, this.productService);
    }

    @Test
    @DisplayName("Should return a wishlist by customerId.")
        void getAllProductsByCustomerIdTest() {
        Wishlist wishlist = ObjectsFactory.getWishlist();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));

        Wishlist wishlistTest = this.wishlistService.getAllProductsByCustomerId(BDDMockito.anyString());

        Assertions.assertThat(wishlistTest.getId()).isEqualTo(wishlist.getId());
        Assertions.assertThat(wishlistTest.getCustomerId()).isEqualTo(wishlist.getCustomerId());
        Assertions.assertThat(wishlistTest.getAmount()).isEqualTo(wishlist.getAmount());
        Assertions.assertThat(wishlistTest.getProducts()).isNotEmpty();

    }

    @Test
    @DisplayName("Must add a product to the wishlist.")
    void addProductToWishlistTest() {
        Product product = ObjectsFactory.getProduct();
        Wishlist wishlist = ObjectsFactory.getWishlist();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));
        BDDMockito.given(this.wishlistRepository.save(BDDMockito.any(Wishlist.class))).willReturn(wishlist);
        BDDMockito.given(this.productService.findProductById(BDDMockito.anyString())).willReturn(product);

        Wishlist wishlistTest = this.wishlistService.addProductToWishlist(wishlist.getCustomerId(), product.getId());

        Assertions.assertThat(wishlistTest.getId()).isEqualTo(wishlist.getId());
        Assertions.assertThat(wishlistTest.getCustomerId()).isEqualTo(wishlist.getCustomerId());
        Assertions.assertThat(wishlistTest.getAmount()).isEqualTo(wishlist.getAmount());

    }

    @Test
    @DisplayName("Should return an exception when adding a product to the wishlist.")
    void notAddProductToWishlistTest() {
        String messageMaxProducts = String.format("Wishlist with more than %d products.", 20);

        Wishlist wishlist = BDDMockito.mock(Wishlist.class);
        Product product = BDDMockito.mock(Product.class);

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));
        BDDMockito.given(this.productService.findProductById(BDDMockito.anyString())).willReturn(product);
        BDDMockito.given(wishlist.addProductToList(product)).willReturn(false);

        Throwable exception = Assertions.catchThrowable(() -> this.wishlistService.addProductToWishlist("1", "1"));

        Assertions.assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage(messageMaxProducts);

    }

    @Test
    @DisplayName("Must remove a product from the wishlist.")
    void removeProductToWishlistTest() {
        Product product = ObjectsFactory.getProduct();
        Wishlist wishlist = ObjectsFactory.getWishlist();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));
        BDDMockito.given(this.wishlistRepository.save(BDDMockito.any(Wishlist.class))).willReturn(wishlist);
        BDDMockito.given(this.productService.findProductById(BDDMockito.anyString())).willReturn(product);

        Wishlist wishlistTest = this.wishlistService.removeProductToWishlist(wishlist.getCustomerId(), product.getId());

        Assertions.assertThat(wishlistTest.getId()).isEqualTo(wishlist.getId());
        Assertions.assertThat(wishlistTest.getAmount()).isEqualTo(BigDecimal.ZERO);
        Assertions.assertThat(wishlistTest.getProducts()).isEmpty();

    }

    @Test
    @DisplayName("Should return an exception when removing a product from an empty wishlist.")
    void notRemoveProductToEmptyWishlistTest() {
        String messageNoProducts = "Wishlist is empty.";

        Wishlist wishlist = BDDMockito.mock(Wishlist.class);
        Product product = BDDMockito.mock(Product.class);

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));
        BDDMockito.given(this.productService.findProductById(BDDMockito.anyString())).willReturn(product);
        BDDMockito.given(wishlist.getProducts()).willReturn(null);

        Throwable exception = Assertions.catchThrowable(() -> this.wishlistService.removeProductToWishlist("1", "1"));

        Assertions.assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage(messageNoProducts);

    }

    @Test
    @DisplayName("Should return true if the product exists in the wishlist. ")
    void consultProductInWishListTest() {
        Product product = ObjectsFactory.getProduct();
        Wishlist wishlist = ObjectsFactory.getWishlist();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));

        boolean result = this.wishlistService.checkProductInWishList(wishlist.getCustomerId(), product.getId());

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false if the product doesn't exists in the wishlist. ")
    void consultFalseProductInWishListTest() throws BusinessException {
        Wishlist wishlist = ObjectsFactory.getWishlist();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));

        boolean result = this.wishlistService.checkProductInWishList(wishlist.getCustomerId(), "2");

        Assertions.assertThat(result).isFalse();
    }
}