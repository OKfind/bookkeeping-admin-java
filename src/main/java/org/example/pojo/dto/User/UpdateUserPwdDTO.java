package org.example.pojo.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author : XR
 * @date :2026/7/3 10:58
 * @description :TODO
 */
@Data
@Schema(description = "更新用户密码参数")
public class UpdateUserPwdDTO {
    @NotNull
    @Schema(description = "用户id")
    private Integer id;

    @NotBlank(message = "旧密码不能为空")
    @Pattern(regexp = "^.{5,16}$", message = "密码长度必须在5到16个字符之间")
    @Schema(description = "旧密码")
    @JsonProperty("old_pwd")
    private String oldPwd;

    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^.{5,16}$", message = "密码长度必须在5到16个字符之间")
    @Schema(description = "新密码")
    @JsonProperty("new_pwd")
    private String newPwd;

    @NotBlank(message = "确认密码不能为空")
    @Pattern(regexp = "^.{5,16}$", message = "密码长度必须在5到16个字符之间")
    @Schema(description = "确认密码")
    @JsonProperty("re_pwd")
    private String rePwd;
}
