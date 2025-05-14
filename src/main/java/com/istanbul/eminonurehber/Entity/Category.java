package com.istanbul.eminonurehber.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_sequence", allocationSize = 1, initialValue = 1)
    private Long id;

    private String name;
    private String description;

    @Column(name = "category_image", columnDefinition = "BYTEA") // PostgreSQL için
    private byte[] categoryImage; // **byte[] olduğuna dikkat et!**
}
