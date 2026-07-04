package org.example.utils;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : XR
 * @date :2026/7/4 15:17
 * @description :TODO
 */
@Slf4j
@Component
public class WechatUtil {
    @Value("${wechat.miniapp.appid}")
    private String APP_ID;

    @Value("${wechat.miniapp.secret}")
    private String APP_SECRET;

    public String getOpenId(String loginCode) {
        try {
            String url = "https://api.weixin.qq.com/sns/jscode2session";
            String requestUrl = url + "?appid=" + APP_ID
                    + "&secret=" + APP_SECRET
                    + "&js_code=" + loginCode
                    + "&grant_type=authorization_code";

            log.info("请求微信登录接口，appid={}", APP_ID);
            HttpResponse response = HttpUtil.createGet(requestUrl).execute();
            String body = response.body();
            log.info("微信接口响应: {}", body);

            JSONObject parseObj = JSONUtil.parseObj(body);

            // 检查微信接口返回的错误
            Object errcode = parseObj.get("errcode");
            if (errcode != null && !"0".equals(errcode.toString())) {
                Object errmsg = parseObj.get("errmsg");
                log.error("微信登录失败, errcode={}, errmsg={}", errcode, errmsg);
                return null;
            }

            Object openid = parseObj.get("openid");
            if (openid == null) {
                log.error("微信接口未返回openid, 响应: {}", body);
                return null;
            }
            return openid.toString();
        } catch (Exception e) {
            log.error("调用微信登录接口异常", e);
            return null;
        }
    }
}
