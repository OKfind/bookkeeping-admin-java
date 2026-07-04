package org.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户实体")
public class User {
    @TableId(type = IdType.AUTO)
    @Schema(description = "用户id")
    private Integer id;

    @Schema(description = "用户名")
    @NotNull
    private String username;

    @JsonIgnore
    @Schema(description = "MD5加盐后的密码")
    @NotNull
    private String password;

    @JsonIgnore
    @Schema(description = "MD5加密的盐值")
    private String salt;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "联系方式")
    private String phone;

    @Schema(description = "用户头像")
    @TableField(value = "user_pic")
    private String userPic;

    @Schema(description = "微信openid")
    @TableField(value = "open_id")
    private String openid;

    @Schema(description = "逻辑删除")
    private Integer deleted;

    @Schema(description = "创建时间")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}
