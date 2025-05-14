package com.istanbul.eminonurehber.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "sliders")
@Data
public class Slider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "slider_image", columnDefinition = "BYTEA",nullable = false) // PostgreSQL için
    private byte[] sliderImage; // **byte[] olduğuna dikkat et!**

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
}
