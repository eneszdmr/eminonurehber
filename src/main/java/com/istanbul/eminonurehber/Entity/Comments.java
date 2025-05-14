package com.istanbul.eminonurehber.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private int rating;

    @ManyToOne
    private User user;

    @ManyToOne
    private Company company;
}
