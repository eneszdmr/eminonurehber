package com.istanbul.eminonurehber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyListForMobileIdName {
    private Long id;
    private String name;
    private String categoryName; // Kategori adı eklendi

}
