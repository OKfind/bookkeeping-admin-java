package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.pojo.Result;
import org.example.pojo.pageMenu;
import org.example.service.pageMenuService;
import org.example.vo.pageMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : XR
 * @date :2026/6/25 08:58
 * @description :TODO
 */
@Tag(name = "页面菜单管理", description = "页面菜单的增删改查")
@RestController
@RequestMapping("/pageMenu")
public class pageMenuController {
    @Autowired
    private pageMenuService pageMenuService;

    @Operation(summary = "获取菜单树形结构")
    @GetMapping
    public Result<List<pageMenuVo>> selectPageMenu() {
        List<pageMenuVo> pmTree = pageMenuService.getPageMenuTree();
        System.out.println(pmTree);
        return Result.success(pmTree);
    }

    @Operation(summary = "获取菜单列表（扁平）")
    @GetMapping("/list")
    public Result<List<pageMenu>> selectPageMenuList() {
        List<pageMenu> pmList = pageMenuService.getPageMenuList();
        return Result.success(pmList);
    }

    @Operation(summary = "新增菜单")
    @PostMapping
    public Result addPageMenu(@Valid @RequestBody pageMenu pageMenu) {
        pageMenuService.addPageMenu(pageMenu);
        return Result.success();
    }

    @Operation(summary = "更新菜单")
    @PutMapping
    public Result updatePageMenu(@Valid @RequestBody pageMenu pageMenu) {
        pageMenuService.updatePageMenu(pageMenu);
        return Result.success();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping
    public Result deletePageMenu(Integer id) {
        pageMenuService.deletePageMenu(id);
        return Result.success();
    }
}
