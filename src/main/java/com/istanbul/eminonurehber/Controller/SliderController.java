package com.istanbul.eminonurehber.Controller;

import com.istanbul.eminonurehber.DTO.SliderDTO;
import com.istanbul.eminonurehber.Entity.Slider;
import com.istanbul.eminonurehber.Service.SliderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sliders")
public class SliderController {

    private final SliderService sliderService;

    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @GetMapping
    @Validated
    @Operation(summary = "Tüm Slider getir")
    public List<SliderDTO> getAllSliders() {
        return sliderService.getAllSliders();
    }

    @PostMapping
    public ResponseEntity<Slider> createSlider(@RequestBody SliderDTO dto) {
        return ResponseEntity.ok(sliderService.createSlider(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Slider> updateSlider(@PathVariable Long id, @RequestBody SliderDTO dto) {
        Slider updatedSlider =sliderService.updateSlider(id, dto);
        return ResponseEntity.ok(updatedSlider);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSlider(@PathVariable Long id) {
        sliderService.deleteSlider(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public Slider getASlider(@PathVariable Long id) {

        return sliderService.findById(id);
    }
}
