package com.istanbul.eminonurehber.Controller;

import com.istanbul.eminonurehber.DTO.CategoryDTO;
import com.istanbul.eminonurehber.DTO.CompanyDTO;
import com.istanbul.eminonurehber.Entity.Category;
import com.istanbul.eminonurehber.Entity.Company;
import com.istanbul.eminonurehber.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Kategori İşlemleri", description = "Kategori CRUD işlemleri")
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Validated
    @Operation(summary = "Tüm kategorileri getir")
    public List<Category> getCategories() {
        List<Category> results = categoryService.getAllCategories();
       // System.out.println("sonuc : "+results.get(0).getName());
        return results;
    }

    @Operation(summary = "Yeni kategori ekle (sadece ADMIN)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN şirket ekleyebilir
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.addCategory(categoryDTO));
    }

    @Operation(summary = "ID ile kategori getir")
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id){
        return categoryService.getOne(id);
    }

    @Operation(summary = "ID ile kategori sil")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

    @Operation(summary = "ID ile kategori düzenle")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory=categoryService.updateCat(id,category);
        return ResponseEntity.ok(updatedCategory);
    }
}
