package com.istanbul.eminonurehber.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductMobileDTO {
    private Long id;
    private String productTitle;
    private String productDescription;
    private double productPrice;
    private String imageBase64;
    private Long companyId;
    private String companyName;
}
