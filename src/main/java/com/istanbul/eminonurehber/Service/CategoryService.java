package com.istanbul.eminonurehber.Service;

import com.istanbul.eminonurehber.DTO.CategoryDTO;
import com.istanbul.eminonurehber.Entity.Category;
import com.istanbul.eminonurehber.Repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class CategoryService {


    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    @Transactional
    public Category addCategory(CategoryDTO categoryDTO) {
            Category category=new Category();
        if (categoryDTO.getImageBase64() != null && !categoryDTO.getImageBase64().isEmpty()) {
            byte[] imageBytes = Base64.getDecoder().decode(categoryDTO.getImageBase64());
            category.setCategoryImage(imageBytes);
            category.setDescription(categoryDTO.getDescription());
            category.setName(categoryDTO.getName());
        }

        return categoryRepository.save(category);
    }

    public Category getOne(Long id){
        return categoryRepository.findById(id).get();
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category updateCat(Long id, Category category) {
        return categoryRepository.findById(id).map(exist ->{
            exist.setName(category.getName());
            exist.setDescription(category.getDescription());
            exist.setCategoryImage(category.getCategoryImage());
            return categoryRepository.save(exist);
        }).orElseThrow(()-> new RuntimeException("Category not found : "+id));
    }
}
