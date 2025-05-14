package com.istanbul.eminonurehber.Service;

import com.istanbul.eminonurehber.DTO.SliderDTO;
import com.istanbul.eminonurehber.Entity.Slider;
import com.istanbul.eminonurehber.Repository.SliderRepository;
import com.istanbul.eminonurehber.utils.SliderDtoConverter;
import com.istanbul.eminonurehber.utils.SliderImageProperties;
import com.istanbul.eminonurehber.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@Service
public class SliderService {

    private final SliderRepository sliderRepository;
    private final SliderDtoConverter dtoConverter;

    public SliderService(SliderRepository sliderRepository,
                         SliderDtoConverter dtoConverter) {
        this.sliderRepository = sliderRepository;
        this.dtoConverter = dtoConverter;
    }

    public List<SliderDTO> getAllSliders() {
        return dtoConverter.convertToDtoList(sliderRepository.findAll());
    }

    public Slider createSlider(SliderDTO dto) {
        Slider slider = dtoConverter.convertToEntity(dto);
        setTimestamps(slider);
        return sliderRepository.save(slider);
    }

    public Slider updateSlider(Long id, SliderDTO dto) {
        Slider existingSlider = getSliderById(id);
        updateSliderFields(existingSlider, dto);
        existingSlider.setUpdatedAt(LocalDateTime.now());
        return sliderRepository.save(existingSlider);
    }

    public void deleteSlider(Long id) {
        sliderRepository.deleteById(id);
    }

    public Slider findById(Long id) {
        return getSliderById(id);
    }

    private Slider getSliderById(Long id) {
        return sliderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Slider not found with id: " + id));
    }

    private void updateSliderFields(Slider slider, SliderDTO dto) {
        slider.setName(dto.getName());
        slider.setDescription(dto.getDescription());
        slider.setOrderIndex(dto.getOrderIndex());
        slider.setSliderImage(dtoConverter.processImage(dto.getImageBase64()));
    }

    private void setTimestamps(Slider slider) {
        LocalDateTime now = LocalDateTime.now();
        slider.setCreatedAt(now);
        slider.setUpdatedAt(now);
    }
}

