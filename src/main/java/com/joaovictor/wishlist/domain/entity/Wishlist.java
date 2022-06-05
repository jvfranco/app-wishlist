package com.joaovictor.wishlist.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Document("wishlist")
public class Wishlist {

    @Id
    private String id;
    private String customerId;
    private List<Product> products;
    private BigDecimal amount;

    public boolean addProductToList(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }

        if (this.products.size() < 20) {
            List<Product> newList = new ArrayList<>();
            newList.addAll(this.products);
            newList.add(product);
            this.products = newList;
            this.sumAmount();
            return true;
        }

        return false;
    }

    public void removeProductToList(Product product) {
        List<Product> newList = new ArrayList<>();
        newList.addAll(this.products);
        newList.remove(product);
        this.products = newList;
        this.sumAmount();
    }

    public void sumAmount() {
        this.amount = this.products
                .stream()
                .map(p -> p.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
