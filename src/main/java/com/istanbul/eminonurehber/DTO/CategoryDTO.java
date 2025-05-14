package com.istanbul.eminonurehber.DTO;

import lombok.Data;

@Data
public class CategoryDTO {

    private Long id;
    private String name;
    private String description;
    private String imageBase64;
}
