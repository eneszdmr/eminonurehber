package com.istanbul.eminonurehber.DTO;


import lombok.Data;

import java.util.List;

@Data
public class CompanyDTO {

    public CompanyDTO() {
    }

    public CompanyDTO(Long id, String name, String address, String phone, String website,
                      Long categoryId, String email,String categoryName,List<CompanyImageDTO> images,
                      String whatsapp,String facebook,String twitter,String instagram,String tiktok,String youtube) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.categoryId = categoryId;
        this.email = email;
        this.categoryName=categoryName;
        this.images = images;
        this.whatsapp = whatsapp;
        this.facebook=facebook;
        this.twitter=twitter;
        this.instagram=instagram;
        this.tiktok = tiktok;
        this.youtube=youtube;

    }

    public CompanyDTO(Long id, String name, String address, String phone,
                      String website, String password, Long categoryId, String email,
                      String categoryName, String latitude, String longitude, List<CompanyImageDTO> images,
                      String whatsapp,String facebook,String twitter,String instagram,String tiktok,String youtube,int clickCount) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.password = password;
        this.categoryId = categoryId;
        this.email = email;
        this.categoryName = categoryName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
        this.whatsapp = whatsapp;
        this.facebook=facebook;
        this.twitter=twitter;
        this.instagram=instagram;
        this.tiktok = tiktok;
        this.youtube=youtube;
        this.clickCount = clickCount;
    }

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String website;
    private String password;
    private Long categoryId;
    private String email; // Kullanıcı adı için gerekli
    private String categoryName; // Kategori adı eklendi

    private String latitude; // Enlem
    private String longitude; // Boylam
    private List<CompanyImageDTO> images;

    private String whatsapp;
    private String instagram;
    private String tiktok;
    private String twitter;
    private String youtube;
    private String facebook;
    private int clickCount;

    public CompanyDTO(String name, int clickCount) {
        this.name = name;
        this.clickCount = clickCount;
    }
}