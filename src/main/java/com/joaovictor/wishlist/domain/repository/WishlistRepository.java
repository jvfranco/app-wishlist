package com.joaovictor.wishlist.domain.repository;

import com.joaovictor.wishlist.domain.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {

    Optional<Wishlist> findByCustomerId(String customerId);
}
