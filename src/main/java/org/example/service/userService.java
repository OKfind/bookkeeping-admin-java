package org.example.service;

import org.example.pojo.entity.User;

public interface userService {
    // 通过用户名查找用户
    User findUserByUsername(String username);

    // 通过openid查找用户
    User findUserByOpenId(String openid);

    // 注册
    void register(String username, String password);

    // 注册微信用户
    void registerWxUser(String openid);

    // 登录
    String login(String username, String password);

    // 微信登录
    String wxLogin(String openid);

    // 获取用户基本信息
    User getUserInfo();

    // 编辑用户基本信息
    void updateUserInfo(Integer id, String username, String nickname, String email, String phone, String userPic);

    // 逻辑删除用户
    void delUser(Integer id);

    // 更新用户密码
    void updatePwd(String oldPwd, String newPwd, String rePwd, Integer id, String token);
}
