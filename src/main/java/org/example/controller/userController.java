package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.pojo.Result;
import org.example.pojo.dto.User.LoginDTO;
import org.example.pojo.dto.User.UpdateUserDTO;
import org.example.pojo.dto.User.UpdateUserPwdDTO;
import org.example.pojo.entity.User;
import org.example.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : XR
 * @date :2026/7/1 09:10
 * @description :TODO
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户信息接口", description = "登录、注册、获取用户基本信息等")
public class userController {
    @Autowired
    private userService userService;

    // 注册用户
    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public Result registerUser(@Valid @RequestBody LoginDTO loginDTO) {
        userService.register(loginDTO.getUsername(), loginDTO.getPassword());
        return Result.success();
    }

    // 登录
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<String> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        String token = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return Result.success(token);
    }

    // 获取用户基本信息
    @Operation(summary = "获取当前登录用户基本信息")
    @GetMapping
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        User user = userService.getUserInfo(token);
        return Result.success(user);
    }

    // 编辑用户基本信息
    @Operation(summary = "编辑用户基本信息")
    @PutMapping
    public Result updateUserInfo(@Valid @RequestBody UpdateUserDTO updateUserDTO) {
        userService.updateUserInfo(updateUserDTO.getId(), updateUserDTO.getUsername(), updateUserDTO.getNickname(),
                updateUserDTO.getEmail(), updateUserDTO.getPhone(), updateUserDTO.getUserPic());
        return Result.success();
    }

    // 删除用户
    @Operation(summary = "逻辑删除用户")
    @DeleteMapping
    public Result deleteUser(@RequestParam Integer id) {
        userService.delUser(id);
        return Result.success();
    }

    // 更新用户密码
    @Operation(summary = "更新用户密码")
    @PutMapping("/updatePwd")
    public Result updatePwd(@RequestBody UpdateUserPwdDTO updateUserPwdDTO,
            @RequestHeader("Authorization") String token) {
        userService.updatePwd(updateUserPwdDTO.getOldPwd(), updateUserPwdDTO.getNewPwd(), updateUserPwdDTO.getRePwd(),
                updateUserPwdDTO.getId(), token);
        return Result.success();
    }
}
