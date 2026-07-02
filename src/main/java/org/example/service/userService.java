package org.example.service;

import org.example.pojo.entity.User;

public interface userService {
    // 通过用户名查找用户
    User findUserByUsername(String username);

    // 注册
    void register(String username,String password);

    // 登录
    String login(String username,String password);

    // 获取用户基本信息
    User getUserInfo();

    // 编辑用户基本信息
    void updateUserInfo(Integer id,String username,String nickname,String email,String phone,String userPic);

    // 逻辑删除用户
    void delUser(Integer id);
}
