package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.exception.ServiceException;
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

    // 根据微信openid查询用户
    @Override
    public User findUserByOpenId(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        return userMapper.selectOne(wrapper);
    }

    // 注册微信用户
    @Override
    public void registerWxUser(String openid) {
        User u = new User();
        u.setUsername("wx_" + openid.substring(0, Math.min(8, openid.length())));
        u.setOpenid(openid);
        u.setPassword("");
        u.setSalt("");
        u.setCreateTime(LocalDateTime.now());
        u.setUpdateTime(LocalDateTime.now());
        userMapper.insert(u);
    }

    // 微信登录
    @Override
    public String wxLogin(String openid) {
        // 根据openid查找用户，不存在则自动注册
        User wxUser = this.findUserByOpenId(openid);
        if (wxUser == null) {
            this.registerWxUser(openid);
            wxUser = this.findUserByOpenId(openid);
        }

        // 生成token
        String token = JWTUtil.generateToken(Long.valueOf(wxUser.getId()), wxUser.getUsername());

        // 缓存到redis
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token, token, 1, TimeUnit.HOURS);

        return token;
    }

    // 注册用户
    @Override
    public void register(String username, String password) {
        if (this.findUserByUsername(username) != null) {
            throw new ServiceException(400, "用户名已存在");
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

    // 登录
    @Override
    public String login(String username, String password) {
        User loginUser = this.findUserByUsername(username);
        // 先判断用户是否存在
        if (loginUser == null) {
            throw new ServiceException(400, "该用户不存在");
        }

        // 如果存在，则将用户手输的密码和查询到的用户加盐字段进行加密
        String md5Pwd = MD5Util.md5WithSalt(password, loginUser.getSalt());

        // 将用户输入的密码加密后，再和查询到的用户加密密码进行比对
        if (!md5Pwd.equals(loginUser.getPassword())) {
            throw new ServiceException(400, "输入的密码不正确");
        }
        String token = JWTUtil.generateToken(Long.valueOf(loginUser.getId()), loginUser.getUsername());

        // 登录成功后需要将token缓存到redis中
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token, token, 1, TimeUnit.HOURS);

        return token;
    }

    // 获取用户基本信息
    @Override
    public User getUserInfo() {
        User u = userMapper.selectOne(null);
        return u;
    }

    // 更新用户基本信息
    @Override
    public void updateUserInfo(Integer id, String username, String nickname, String email, String phone,
            String userPic) {
        if (this.findUserByUsername(username) != null) {
            throw new ServiceException(400, "用户名已存在");
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

    // 更新用户密码
    @Override
    public void updatePwd(String oldPwd, String newPwd, String rePwd, Integer id, String token) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ServiceException(400, "该用户不存在");
        }

        // 先判断旧密码是否正确
        if (!MD5Util.verifyWithSalt(oldPwd, user.getSalt(), user.getPassword())) {
            throw new ServiceException(400, "旧密码不正确");
        }

        // 判断旧密码和新密码是否输入相同
        if (oldPwd.equals(newPwd)) {
            throw new ServiceException(400, "旧密码不能与新密码输入相同的值");
        }

        // 再判断两次输入的新密码是否一样
        if (!newPwd.equals(rePwd)) {
            throw new ServiceException(400, "两次输入的新密码不一致");
        }

        // 如果都通过以上判断，则可以修改密码了
        String md5NewPwd = MD5Util.md5WithSalt(newPwd, user.getSalt());
        user.setId(id);
        user.setPassword(md5NewPwd);
        userMapper.updateById(user);

        // 修改成功后，需要把缓存到redis中的旧token删除
        stringRedisTemplate.delete(token);
    }
}
