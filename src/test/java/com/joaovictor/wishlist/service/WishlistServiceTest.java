package com.joaovictor.wishlist.service;

import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.entity.Wishlist;
import com.joaovictor.wishlist.domain.repository.WishlistRepository;
import com.joaovictor.wishlist.exception.BusinessException;
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
    @DisplayName("Must return wishlist by customerId.")
    public void getAllProductsByCustomerIdTest() throws BusinessException {
        String customerId = "1", productId = "1", id = "1";
        String description = "Product 01";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description(description).price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).customerId(customerId).products(List.of(product)).amount(value).build();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(customerId)).willReturn(Optional.of(wishlist));

        Wishlist wishlist1 = this.wishlistService.getAllProductsByCustomerId(customerId);

        Assertions.assertThat(wishlist1.getId()).isEqualTo(id);
        Assertions.assertThat(wishlist1.getCustomerId()).isEqualTo(customerId);
        Assertions.assertThat(wishlist1.getAmount()).isEqualTo(value);
        Assertions.assertThat(wishlist1.getProducts()).isNotEmpty();

    }

    @Test
    @DisplayName("Must add a product to the wishlist.")
    public void addProductToWishlistTest() throws BusinessException {
        String customerId = "1", productId = "1", id = "1";
        String description = "Product 01";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description(description).price(value).build();
        Product product2 = Product.builder().id(productId).description(description).price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).products(List.of(product)).amount(value).customerId(customerId).build();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(customerId)).willReturn(Optional.of(wishlist));
        BDDMockito.given(this.productService.findProductById(productId)).willReturn(product2);

        Wishlist wishlist1 = this.wishlistService.addProductToWishlist(customerId, productId);

        Assertions.assertThat(wishlist1.getId()).isEqualTo(id);
        Assertions.assertThat(wishlist1.getCustomerId()).isEqualTo(customerId);
        Assertions.assertThat(wishlist1.getAmount()).isEqualTo(value.multiply(BigDecimal.valueOf(2)));
        Assertions.assertThat(wishlist1.getProducts()).isNotEmpty();

    }

    @Test
    @DisplayName("Should return a exception when add a product to the wishlist.")
    public void notAddProductToWishlistTest() {
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
    @DisplayName("Must remove a product to the wishlist.")
    public void removeProductToWishlistTest() throws BusinessException {
        String customerId = "1", productId = "1", id = "1";
        String description = "Product 01";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description(description).price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).products(List.of(product)).amount(value).customerId(customerId).build();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(customerId)).willReturn(Optional.of(wishlist));
        BDDMockito.given(this.productService.findProductById(productId)).willReturn(product);

        Wishlist wishlist1 = this.wishlistService.removeProductToWishlist(customerId, productId);

        Assertions.assertThat(wishlist1.getId()).isEqualTo(id);
        Assertions.assertThat(wishlist1.getAmount()).isEqualTo(BigDecimal.ZERO);
        Assertions.assertThat(wishlist1.getProducts()).isEmpty();

    }

    @Test
    @DisplayName("Should return a exception when remove a product to the wishlist.")
    public void notRemoveProductToWishlistTest() {
        String messageProductNotExist = "Product does not exist in this wishlist";

        Wishlist wishlist = BDDMockito.mock(Wishlist.class);
        Product product = BDDMockito.mock(Product.class);
        List<Product> products = BDDMockito.mock(ArrayList.class);

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));
        BDDMockito.given(this.productService.findProductById(BDDMockito.anyString())).willReturn(product);
        BDDMockito.given(wishlist.getProducts()).willReturn(products);
        BDDMockito.given(wishlist.getProducts().contains(product)).willReturn(false);

        Throwable exception = Assertions.catchThrowable(() -> this.wishlistService.removeProductToWishlist("1", "1"));

        Assertions.assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage(messageProductNotExist);

    }

    @Test
    @DisplayName("Should return a exception when remove a product to the empty wishlist.")
    public void notRemoveProductToEmptyWishlistTest() {
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
    @DisplayName("Should be return true if the product exists in the wishlist. ")
    public void consultProductInWishListTest() throws BusinessException {
        String customerId = "1", productId = "1", id = "1";
        String description = "Product 01";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description(description).price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).products(List.of(product)).amount(value).customerId(customerId).build();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));

        boolean result = this.wishlistService.consultProductInWishList(customerId, productId);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should be return false if the product not exists in the wishlist. ")
    public void consultFalseProductInWishListTest() throws BusinessException {
        String customerId = "1", productId = "1", id = "1";
        String productId2 = "2";
        String description = "Product 01";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description(description).price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).products(List.of(product)).amount(value).customerId(customerId).build();

        BDDMockito.given(this.wishlistRepository.findByCustomerId(BDDMockito.anyString())).willReturn(Optional.of(wishlist));

        boolean result = this.wishlistService.consultProductInWishList(customerId, productId2);

        Assertions.assertThat(result).isFalse();
    }
}