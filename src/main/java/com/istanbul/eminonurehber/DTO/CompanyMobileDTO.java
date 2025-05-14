package com.istanbul.eminonurehber.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyMobileDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String website;
    private Long categoryId;
    private String email; // Kullanıcı adı için gerekli
    private String categoryName; // Kategori adı eklendi
    private String latitude; // Enlem
    private String longitude; // Boylam
}
