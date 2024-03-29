package com.PowerBike.controller;

import com.PowerBike.dto.CategoryDto;
import com.PowerBike.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/getCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getCategory (@PathVariable long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto dto){
        return categoryService.saveCategory(dto);
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable long id ,@RequestBody CategoryDto dto){
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable long id){
        return categoryService.deleteCategory(id);
    }
}
