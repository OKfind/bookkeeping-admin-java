package org.example.pojo.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "登录/注册请求参数")
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^.{2,16}$", message = "用户名长度必须在2到16个字符之间")
    @Schema(description = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^.{5,16}$", message = "密码长度必须在5到16个字符之间")
    @Schema(description = "密码")
    private String password;
}
