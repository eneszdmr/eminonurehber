package com.istanbul.eminonurehber.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    @SequenceGenerator(name = "company_seq", sequenceName = "company_sequence", allocationSize = 1, initialValue = 1000)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String website;
    private String facebook;
    private String whatsapp;
    private String instagram;
    private String tiktok;
    private String twitter;
    private String youtube;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;

    @OneToMany(mappedBy = "company")
    private List<Comments> comments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Kullanıcı ile bağlantı

    private Double latitude; // Enlem
    private Double longitude; // Boylam

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CompanyImage> images = new ArrayList<>();  // Mutable liste!

    //firma tıklanma sayısı
    @Column(nullable = false)
    private int clickCount = 0;





}
