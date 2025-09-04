package dev.noteforge.knowhub.category.controller;

import dev.noteforge.knowhub.category.domain.Category;
import dev.noteforge.knowhub.post.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 카테고리 API
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping("")
    public List<Category> getCategories() {
        return categoryService.findAllCategory();
    }
}
