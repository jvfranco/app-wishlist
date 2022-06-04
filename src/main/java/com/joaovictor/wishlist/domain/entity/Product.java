package com.joaovictor.wishlist.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Document("product")
public class Product implements Serializable {

    @Id
    private String id;
    private String description;
    private BigDecimal price;

}
