package ru.romzhel.eshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.romzhel.eshop.entities.Category;
import ru.romzhel.eshop.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(new Category());
    }

    public boolean save(Category category) {
        categoryRepository.save(category);
        return true;
    }

    public boolean removeCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
        return true;
    }

    public boolean removeCategory(Category category) {
        categoryRepository.delete(category);
        return true;
    }
}
