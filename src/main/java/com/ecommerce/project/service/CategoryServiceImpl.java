package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    //private List<Category> categories = new ArrayList<>();
    private static final AtomicLong counter = new AtomicLong(0);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        long timestamp = System.currentTimeMillis(); // Get current time in ms
        long uniqueCounter = counter.getAndIncrement(); // Increment counter
        long uniqueId = timestamp * 1000 + (uniqueCounter % 1000);
        category.setCategoryId(uniqueId);
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId){
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found"));

        categoryRepository.delete(category);
        return "Category has been removed";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId){
        List<Category> categories = categoryRepository.findAll();
        Optional<Category> currCategory = categories.stream()
                .filter(c -> Objects.equals(c.getCategoryId(), categoryId))
                .findFirst();

        if(currCategory.isPresent()){
            Category existingCategory = currCategory.get();
            existingCategory.setCategoryName((category.getCategoryName()));
            Category savedCategory = categoryRepository.save(existingCategory);
            return savedCategory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found");
        }
    }
}
