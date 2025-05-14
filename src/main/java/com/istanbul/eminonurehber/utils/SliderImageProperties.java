package com.istanbul.eminonurehber.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "slider.image")
public class SliderImageProperties {

    private int maxSize;
    private double scale;
    private String format;
}
