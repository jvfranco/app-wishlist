package com.joaovictor.wishlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaovictor.wishlist.domain.entity.Product;
import com.joaovictor.wishlist.domain.entity.Wishlist;
import com.joaovictor.wishlist.domain.entity.dto.RequestDTO;
import com.joaovictor.wishlist.factory.ObjectsFactory;
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
    void getAllProductsByIdTest() throws Exception {
        Wishlist wishlist = ObjectsFactory.getWishlist();

        BDDMockito.given(this.wishlistService.getAllProductsByCustomerId(BDDMockito.anyString())).willReturn(wishlist);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(WISHLIST_API.concat(String.format("/all/%s", wishlist.getCustomerId())))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("customerId").value(wishlist.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("products", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("amount").value(wishlist.getAmount()));
    }

    @Test
    @DisplayName("Must be add a product to the list.")
    void addProductToWishlistTest() throws Exception {
        Product product = ObjectsFactory.getProduct();
        Wishlist wishlist = ObjectsFactory.getWishlist();

        BDDMockito.given(this.wishlistService.addProductToWishlist(BDDMockito.anyString(), BDDMockito.anyString())).willReturn(wishlist);

        String json = new ObjectMapper().writeValueAsString(new RequestDTO(wishlist.getCustomerId(), product.getId()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(WISHLIST_API.concat("/addProduct"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("customerId").value(wishlist.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("products").isNotEmpty());
    }

    @Test
    @DisplayName("Must remove a product from the wishlist.")
    void removeProductToWishlistTest() throws Exception {
        Wishlist wishlist = ObjectsFactory.getWishlistEmpty();

        BDDMockito.given(this.wishlistService.removeProductToWishlist(BDDMockito.anyString(), BDDMockito.anyString())).willReturn(wishlist);

        String json = new ObjectMapper().writeValueAsString(new RequestDTO(wishlist.getCustomerId(), "1"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(WISHLIST_API.concat("/removeProduct"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("customerId").value(wishlist.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("products").isEmpty());
    }

    @Test
    @DisplayName("Must check if a product exists in the wishlist.")
    void consultProductInWishlistTest() throws Exception {
        BDDMockito.given(this.wishlistService.checkProductInWishList(BDDMockito.anyString(), BDDMockito.anyString())).willReturn(true);

        String json = new ObjectMapper().writeValueAsString(new RequestDTO(BDDMockito.anyString(), BDDMockito.anyString()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(WISHLIST_API.concat("/checkProduct"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

}