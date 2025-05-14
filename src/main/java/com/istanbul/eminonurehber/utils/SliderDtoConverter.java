package com.istanbul.eminonurehber.utils;

import com.istanbul.eminonurehber.DTO.SliderDTO;
import com.istanbul.eminonurehber.Entity.Slider;
import com.istanbul.eminonurehber.exceptions.ImageProcessingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SliderDtoConverter {

    private final SliderImageProperties imageProperties;

    public SliderDtoConverter(@Qualifier("slider.image-com.istanbul.eminonurehber.utils.SliderImageProperties") SliderImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    public SliderDTO convertToDto(Slider slider) {
        SliderDTO dto = new SliderDTO();
        dto.setId(slider.getId());
        dto.setName(slider.getName());
        dto.setDescription(slider.getDescription());
        dto.setOrderIndex(slider.getOrderIndex());
        dto.setImageBase64(convertImageToBase64(slider.getSliderImage()));
        return dto;
    }

    public List<SliderDTO> convertToDtoList(List<Slider> sliders) {
        return sliders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private String convertImageToBase64(byte[] imageBytes) {
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    public Slider convertToEntity(SliderDTO dto) {
        Slider slider = new Slider();
        slider.setName(dto.getName());
        slider.setDescription(dto.getDescription());
        slider.setOrderIndex(dto.getOrderIndex());
        slider.setSliderImage(processImage(dto.getImageBase64()));
        return slider;
    }

    public byte[] processImage(String base64Image) {
        String imageData = base64Image.startsWith("data:") ?
                base64Image.substring(base64Image.indexOf(',') + 1) :
                base64Image;

        byte[] imageBytes = Base64.getDecoder().decode(imageData);

        if (imageBytes.length > imageProperties.getMaxSize()) {
            try {
                return Utils.compressImage(
                        imageBytes,
                        imageProperties.getFormat(),
                        (float) imageProperties.getScale(),
                        imageProperties.getMaxSize()
                );
            } catch (IOException e) {
                throw new ImageProcessingException("Görsel sıkıştırılırken hata oluştu", e);
            }
        }
        return imageBytes;
    }
}