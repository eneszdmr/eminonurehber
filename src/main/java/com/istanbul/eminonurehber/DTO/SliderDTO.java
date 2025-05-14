package com.istanbul.eminonurehber.DTO;

import lombok.Data;

@Data
public class SliderDTO {

    private Long id;
    private String name;
    private String description;
    private String imageBase64;
    private Integer orderIndex;

}
