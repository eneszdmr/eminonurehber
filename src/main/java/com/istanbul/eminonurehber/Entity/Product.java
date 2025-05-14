package com.istanbul.eminonurehber.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productTitle;
    private String productDescription;
    private double productPrice;

    @Column(name = "product_image", columnDefinition = "BYTEA") // PostgreSQL için
    private byte[] productImage; // **byte[] olduğuna dikkat et!**

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
