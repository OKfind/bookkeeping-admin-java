package org.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author : XR
 * @date :2026/6/24 16:41
 * @description :TODO
 */
@Data
public class pageMenu {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotNull(message = "父节点为必填")
    @JsonProperty("parent_id")
    private Integer parentId;
    @NotBlank(message = "页面名称不能为空")
    @JsonProperty("menu_name")
    private String menuName;
    private String path;
    private Integer sort;
    private Integer disabled;
}
