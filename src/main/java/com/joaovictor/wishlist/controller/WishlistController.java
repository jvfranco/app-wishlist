package com.joaovictor.wishlist.controller;

import com.joaovictor.wishlist.domain.entity.dto.RequestDTO;
import com.joaovictor.wishlist.domain.entity.dto.WishlistDTO;
import com.joaovictor.wishlist.exception.BusinessException;
import com.joaovictor.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/wishlist")
@AllArgsConstructor
@Tag(name = "Wishlist API")
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping(value = "/products/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Wishlist with  all products.")
    public WishlistDTO getAllProductsById(@PathVariable String customerId) {
        var wishlist = this.wishlistService.getAllProductsByCustomerId(customerId);
        return new WishlistDTO(wishlist.getCustomerId(), wishlist.getProducts(), wishlist.getAmount());
    }

    @PostMapping(value = "/addProduct")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a product to the wishlist.")
    public WishlistDTO addProductToWishlist(@RequestBody @Valid RequestDTO requestDTO) {
        var wishlist = this.wishlistService.addProductToWishlist(requestDTO.customerId(), requestDTO.productId());
        return new WishlistDTO(wishlist.getCustomerId(), wishlist.getProducts(), wishlist.getAmount());
    }

    @PostMapping(value = "/removeProduct")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Remove a product to the wishlist.")
    public WishlistDTO removeProductToWishlist(@RequestBody @Valid RequestDTO requestDTO) {
        var wishlist = this.wishlistService.removeProductToWishlist(requestDTO.customerId(), requestDTO.productId());
        return new WishlistDTO(wishlist.getCustomerId(), wishlist.getProducts(), wishlist.getAmount());
    }

    @PostMapping(value = "/consultProduct")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Check if a product is already in the wishlist.")
    public boolean consultProductInWishlist(@RequestBody @Valid RequestDTO requestDTO) {
        return this.wishlistService.consultProductInWishList(requestDTO.customerId(), requestDTO.productId());
    }

}
