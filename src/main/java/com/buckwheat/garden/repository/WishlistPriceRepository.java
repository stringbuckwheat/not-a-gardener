package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.WishlistPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistPriceRepository extends JpaRepository<WishlistPrice, Integer> {
}
