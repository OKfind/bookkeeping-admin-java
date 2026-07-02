package org.example.service;

import org.example.pojo.pageMenu;
import org.example.vo.pageMenuVo;

import java.util.List;

public interface pageMenuService {
    // 获取页面树形结构
    List<pageMenuVo> getPageMenuTree();

    // 获取页面列表
    List<pageMenu> getPageMenuList();

    // 新增页面路径
    void addPageMenu(pageMenu pageMenu);

    // 更新页面路径
    void updatePageMenu(pageMenu pageMenu);

    // 删除页面路径
    void deletePageMenu(Integer id);
}
