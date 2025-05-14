package com.istanbul.eminonurehber.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_data", columnDefinition = "BYTEA") // PostgreSQL için
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private Company company;
}
