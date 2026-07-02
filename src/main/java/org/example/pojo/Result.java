package org.example.pojo;

/**
 * @author : XR
 * @date :2026/6/24 16:45
 * @description :TODO
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果封装类
 * @param <T> 返回数据的泛型类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private Integer code;

    /** 提示信息 */
    private String message;

    /** 返回的数据 */
    private T data;

    /** 时间戳（可选，方便前端或客户端校验） */
    private Long timestamp;

    /** 私有构造方法，防止外部直接 new */
    private Result() {
        this.timestamp = System.currentTimeMillis();
    }

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ==================== 成功响应方法 ====================

    /** 成功：不带数据 */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /** 成功：带数据 */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /** 成功：自定义提示信息 */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // ==================== 失败响应方法 ====================

    /** 失败：自定义状态码和提示信息 */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /** 失败：默认 500 错误 */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

}