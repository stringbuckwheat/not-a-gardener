package com.buckwheat.garden.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "wishlist_price")
public class WishlistPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int wishlistPriceNo;

    private String store;

    @Column(columnDefinition = "TEXT")
    private String link;

    // FK
    @ManyToOne
    @JoinColumn(name = "wishlist_no")
    private Wishlist wishlist;
}
