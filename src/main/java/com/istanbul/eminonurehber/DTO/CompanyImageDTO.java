package com.istanbul.eminonurehber.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"company"})
public class CompanyImageDTO {
    private String imageBase64;
}
