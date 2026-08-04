package org.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 账单分类
 */
@Data
@Schema(description = "账单分类")
public class Category {

    @Schema(description = "账单分类id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "账单分类名称（餐饮，打车等）")
    private String name;

    @Schema(description = "账单类别（1：收入，2：支出）")
    private Integer type;

    @Schema(description = "UI展示（例如 emoji 或 icon url）")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

}
