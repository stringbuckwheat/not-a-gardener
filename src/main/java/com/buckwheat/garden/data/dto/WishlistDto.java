package com.buckwheat.garden.data.dto;

import lombok.Data;

@Data
public class WishlistDto {
    private String plantName;
    private String detail;
    private String username;
    private String store;
    private String link;
    private int price;
}
