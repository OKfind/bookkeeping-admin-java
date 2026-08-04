package org.example.controller;

import org.example.pojo.Result;
import org.example.pojo.entity.Category;
import org.example.service.categoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : XR
 * @date :2026/8/3 16:42
 * @description :TODO
 */
@RestController
@RequestMapping("/category")
public class categoryController {
    @Autowired
    private categoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> getAllCategoryList(){
        List<Category> categories = categoryService.getCategoryList();
        return Result.success(categories);
    }
}
