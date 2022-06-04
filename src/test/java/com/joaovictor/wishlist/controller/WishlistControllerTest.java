package com.joaovictor.wishlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.entity.Wishlist;
import com.joaovictor.wishlist.domain.entity.dto.RequestDTO;
import com.joaovictor.wishlist.service.ProductService;
import com.joaovictor.wishlist.service.WishlistService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = WishlistController.class)
@AutoConfigureMockMvc
class WishlistControllerTest {

    static String WISHLIST_API = "/api/wishlist";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WishlistService wishlistService;

    @MockBean
    ProductService productService;

    @Test
    @DisplayName("Should return a wishlist with all products.")
    public void getAllProductsByIdTest() throws Exception {
        String id = "1";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(id).description("Product 01").price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).customerId(id).products(List.of(product)).amount(value).build();

        BDDMockito.given(this.wishlistService.getAllProductsByCustomerId(id)).willReturn(wishlist);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(WISHLIST_API.concat(String.format("/products/%s", id)))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("customerId").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("products", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("amount").value(value));
    }

    @Test
    @DisplayName("Must be add a product to the list.")
    public void addProductToWishlistTest() throws Exception {
        String id = "1";
        String productId = "1";
        String customerId = "1";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description("Product 01").price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).customerId(customerId).products(List.of(product)).amount(value).build();

        BDDMockito.given(this.wishlistService.addProductToWishlist(customerId, productId)).willReturn(wishlist);

        String json = new ObjectMapper().writeValueAsString(new RequestDTO(customerId, productId));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(WISHLIST_API.concat("/addProduct"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("customerId").value(customerId))
                .andExpect(MockMvcResultMatchers.jsonPath("products").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("amount").value(value));
    }

    @Test
    @DisplayName("Must remove a product from the wishlist.")
    public void removeProductToWishlistTest() throws Exception {
        String id = "1";
        String productId = "1";
        String customerId = "1";
        BigDecimal value = BigDecimal.ZERO;
        Wishlist wishlist = Wishlist.builder().id(id).customerId(customerId).amount(value).build();

        BDDMockito.given(this.wishlistService.removeProductToWishlist(customerId, productId)).willReturn(wishlist);

        String json = new ObjectMapper().writeValueAsString(new RequestDTO(customerId, productId));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(WISHLIST_API.concat("/removeProduct"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("customerId").value(customerId))
                .andExpect(MockMvcResultMatchers.jsonPath("products").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("amount").value(value));
    }

    @Test
    @DisplayName("Must check if a product exists in the wishlist.")
    public void consultProductInWishlistTest() throws Exception {
        String id = "1";
        String productId = "1";
        String customerId = "1";
        BigDecimal value = BigDecimal.valueOf(10.0);
        Product product = Product.builder().id(productId).description("Product 01").price(value).build();
        Wishlist wishlist = Wishlist.builder().id(id).customerId(customerId).products(List.of(product)).amount(value).build();

        BDDMockito.given(this.wishlistService.consultProductInWishList(customerId, productId)).willReturn(true);

        String json = new ObjectMapper().writeValueAsString(new RequestDTO(customerId, productId));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(WISHLIST_API.concat("/consultProduct"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

}