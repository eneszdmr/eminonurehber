package com.istanbul.eminonurehber.Repository;

import com.istanbul.eminonurehber.Entity.Slider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SliderRepository extends JpaRepository<Slider, Long> {
    List<Slider> findAllByOrderByOrderIndexAsc();
}
