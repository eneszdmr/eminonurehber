package com.istanbul.eminonurehber.DTO;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String productTitle;
    private String productDescription;
    private double productPrice;
    private String imageBase64;
}
