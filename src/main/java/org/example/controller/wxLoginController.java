package org.example.controller;

import org.example.pojo.Result;
import org.example.service.userService;
import org.example.utils.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : XR
 * @date :2026/7/4 15:38
 * @description :TODO
 */
@RestController
@RequestMapping("/wx")
public class wxLoginController {
    @Autowired
    private WechatUtil wechatUtil;

    @Autowired
    private userService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> wxLogin(@RequestBody Map<String, String> params) {
        // 获取前端传递过来的code，然后后端请求微信提供的接口，得到openid
        String code = params.get("code");
        if (code == null || code.isBlank()) {
            return Result.fail(400, "code参数不能为空");
        }

        String openid = wechatUtil.getOpenId(code);

        if (openid == null) {
            return Result.fail(400, "微信登录失败，请检查code是否有效");
        }

        // 微信登录/注册，返回token
        String token = userService.wxLogin(openid);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("openid", openid);
        return Result.success(data);
    }
}
