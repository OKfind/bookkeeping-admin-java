package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.mapper.userMapper;
import org.example.pojo.entity.User;
import org.example.service.userService;
import org.example.utils.JWTUtil;
import org.example.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : XR
 * @date :2026/6/30 16:56
 * @description :TODO
 */
@Service
public class userServiceImpl implements userService {
    @Autowired
    private userMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 根据用户名称查询用户
    @Override
    public User findUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User u = userMapper.selectOne(wrapper);
        return u;
    }

    // 注册用户
    @Override
    public void register(String username, String password) {
        if (this.findUserByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }
        String salt = UUID.randomUUID().toString().substring(0, 6); // 生成随机盐
        String md5Pwd = MD5Util.md5WithSalt(password, salt); // 加密密码

        User u = new User();
        u.setUsername(username);
        u.setPassword(md5Pwd);
        u.setSalt(salt);
        u.setCreateTime(LocalDateTime.now());
        u.setUpdateTime(LocalDateTime.now());
        userMapper.insert(u);
    }

    @Override
    public String login(String username, String password) {
        User loginUser = this.findUserByUsername(username);
        // 先判断用户是否存在
        if(loginUser == null){
            throw  new RuntimeException("该用户不存在");
        }

        // 如果存在，则将用户手输的密码和查询到的用户加盐字段进行加密
        String md5Pwd = MD5Util.md5WithSalt(password,loginUser.getSalt());

        // 将用户输入的密码加密后，再和查询到的用户加密密码进行比对
        if(!md5Pwd.equals(loginUser.getPassword())){
            throw new RuntimeException("输入的密码不正确");
        }
        String token = JWTUtil.generateToken(Long.valueOf(loginUser.getId()),loginUser.getUsername());

        // 登录成功后需要将token缓存到redis中
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
        operations.set(token,token,1, TimeUnit.HOURS);

        return token;
    }

    // 获取用户基本信息
    @Override
    public User getUserInfo() {
        User u = userMapper.selectOne(null);
        return u;
    }

    @Override
    public void updateUserInfo(Integer id,String username, String nickname, String email, String phone, String userPic) {
        if (this.findUserByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUserPic(userPic);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);
    }

    // 逻辑删除用户
    @Override
    public void delUser(Integer id) {
        userMapper.deleteById(id);
    }
}
