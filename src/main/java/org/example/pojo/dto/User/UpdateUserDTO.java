package org.example.pojo.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author : XR
 * @date :2026/7/1 17:43
 * @description :TODO
 */
@Data
@Schema(description = "编辑用户参数")
public class UpdateUserDTO {
    @Schema(description = "用户id")
    @NotNull
    private Integer id;

    @Schema(description = "用户名")
    @NotNull
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "联系方式")
    private String phone;

    @Schema(description = "用户头像")
    private String userPic;
}
