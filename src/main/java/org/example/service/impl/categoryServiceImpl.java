package org.example.service.impl;

import org.example.mapper.categoryMapper;
import org.example.pojo.entity.Category;
import org.example.service.categoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : XR
 * @date :2026/8/3 16:38
 * @description :TODO
 */
@Service
public class categoryServiceImpl implements categoryService {
    @Autowired
    private categoryMapper categoryMapper;

    /**
     * 获取账单分类列表
     * @return
     */
    @Override
    public List<Category> getCategoryList() {
        List<Category> categories = categoryMapper.selectList(null);
        return categories;
    }
}
