package org.example.service;

import org.example.pojo.entity.Category;

import java.util.List;

public interface categoryService {
    // 获取账单分类列表
    List<Category> getCategoryList();
}
