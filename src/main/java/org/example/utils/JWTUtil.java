package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : XR
 * @date :2026/7/1 14:04
 * @description :TODO
 */
@Slf4j
public class JWTUtil {
    // 密钥（至少32个字符，满足HMAC-SHA256要求）
    private static final String SECRET = "my-bookkeeping-by-hugo-2026-secret-key!";
    // Token 过期时间：例如 1 小时（单位：毫秒）
    private static final long EXPIRATION = 1 * 60 * 60 * 1000L;

    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 1. 生成 Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT 字符串
     */
    public static String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", username);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 2. 解析 Token，获取 Claims 信息
     *
     * @param token JWT 字符串
     * @return Claims 对象，如果解析失败返回 null
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Token 解析或验证失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 3. 验证 Token 是否有效且未过期
     *
     * @param token JWT 字符串
     * @return true: 有效, false: 无效或已过期
     */
    public static boolean validateToken(String token) {
        Claims claims = parseToken(token);
        return claims != null && claims.getExpiration().after(new Date());
    }

    /**
     * 4. 从 Token 中获取用户 id
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.get("id", Long.class);
        }
        return null;
    }

    /**
     * 5. 从 Token 中获取用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.get("username", String.class);
        }
        return null;
    }
}
