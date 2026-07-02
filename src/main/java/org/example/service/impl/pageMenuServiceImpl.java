package org.example.service.impl;

import org.example.mapper.pageMenuMapper;
import org.example.pojo.pageMenu;
import org.example.service.pageMenuService;
import org.example.vo.pageMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : XR
 * @date :2026/6/25 09:03
 * @description :TODO
 */
@Service
public class pageMenuServiceImpl implements pageMenuService {
    @Autowired
    private pageMenuMapper pageMenuMapper;

    // 获取页面树形结构
    @Override
    public List<pageMenuVo> getPageMenuTree() {
        List<pageMenu> list = pageMenuMapper.selectList(null);
        return buildPageMenuTree(list, 0);
    }

    // 创建页面菜单树形结构
    public List<pageMenuVo> buildPageMenuTree(List<pageMenu> list, Integer parentId) {
        List<pageMenuVo> pageMenuTree = new ArrayList<>();
        for (pageMenu menu : list) {
            if (menu.getParentId().equals(parentId)) {
                pageMenuVo pmVo = new pageMenuVo();
                pmVo.setId(menu.getId());
                pmVo.setMenuName(menu.getMenuName());
                pmVo.setSort(menu.getSort());
                pmVo.setDisabled(menu.getDisabled());
                pmVo.setChildren(buildPageMenuTree(list, menu.getId()));
                pageMenuTree.add(pmVo);
            }
        }
        return pageMenuTree;
    }

    // 获取页面列表
    @Override
    public List<pageMenu> getPageMenuList() {
        List<pageMenu> pageMenuList = pageMenuMapper.selectList(null);
        return pageMenuList;
    }

    // 新增页面路径
    @Override
    public void addPageMenu(pageMenu pageMenu) {
        pageMenuMapper.insert(pageMenu);
    }

    @Override
    public void updatePageMenu(pageMenu pageMenu) {
        pageMenuMapper.updateById(pageMenu);
    }

    @Override
    public void deletePageMenu(Integer id) {
        pageMenuMapper.deleteById(id);
    }
}
