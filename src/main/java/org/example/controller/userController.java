package org.example.controller;

import jakarta.validation.Valid;
import org.example.pojo.Result;
import org.example.pojo.dto.User.LoginDTO;
import org.example.pojo.dto.User.UpdateUserDTO;
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
public class userController {
    @Autowired
    private userService userService;

    // 注册用户
    @PostMapping("/register")
    public Result registerUser(@Valid @RequestBody LoginDTO loginDTO) {
        userService.register(loginDTO.getUsername(), loginDTO.getPassword());
        return Result.success();
    }

    // 登录
    @PostMapping("/login")
    public Result<String> loginUser(@Valid @RequestBody LoginDTO loginDTO){
        String token = userService.login(loginDTO.getUsername(),loginDTO.getPassword());
        return Result.success(token);
    }

    // 获取用户基本信息
    @GetMapping
    public Result<User> getUserInfo(){
        User user = userService.getUserInfo();
        return Result.success(user);
    }

    // 编辑用户基本信息
    @PutMapping
    public Result updateUserInfo(@Valid @RequestBody UpdateUserDTO updateUserDTO){
        userService.updateUserInfo(updateUserDTO.getId(),updateUserDTO.getUsername(),updateUserDTO.getNickname(),updateUserDTO.getEmail(),updateUserDTO.getPhone(),updateUserDTO.getUserPic());
        return Result.success();
    }

    // 删除用户
    @DeleteMapping
    public Result deleteUser(@RequestParam Integer id){
        userService.delUser(id);
        return Result.success();
    }
}
