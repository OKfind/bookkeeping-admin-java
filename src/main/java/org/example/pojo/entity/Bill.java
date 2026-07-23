package org.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.pojo.enums.BillPayType;

import java.util.Date;

/**
 * 账单
 */
@Data
@Schema(description = "账单实体")
public class Bill {
    @Schema(description = "账单id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "账单归属用户")
    @NotNull(message = "用户id不能为空")
    @JsonProperty("user_id")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "账单类型（1：收入，2：支出）")
    @NotNull(message = "账单类型不能为空")
    @Min(value = 1, message = "账单类型只能是1或2")
    @Max(value = 2, message = "账单类型只能是1或2")
    private Integer type;

    @Schema(description = "支付类型（1：现金，2：微信支付，3：支付宝，4：信用卡，5：储蓄卡，6：其它）",
            allowableValues = {"1", "2", "3", "4", "5", "6"})
    @NotNull(message = "支付类型不能为空")
    @JsonProperty("pay_type")
    @TableField(value = "pay_type")
    @Min(value = 1, message = BillPayType.LIMIT_MESSAGE)
    @Max(value = 6, message = BillPayType.LIMIT_MESSAGE)
    private Integer payType;

    @Schema(description = "账单图片")
    @JsonProperty("bill_img")
    @TableField(value = "bill_img")
    private String billImg;

    @Schema(description = "账单金额")
    @NotNull(message = "账单金额不能为空")
    @DecimalMin(value = "0.01", message = "账单金额必须大于0")
    private Double amount;

    @Schema(description = "账单类型id")
    @NotNull(message = "账单类型id不能为空")
    @JsonProperty("category_id")
    @TableField(value = "category_id")
    private Long categoryId;

    @Schema(description = "账单备注")
    private String remark;

    @Schema(description = "账单发生时间")
    @NotNull(message = "账单发生时间不能为空")
    @JsonProperty("bill_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "bill_time")
    private Date billTime;

    @Schema(description = "创建时间")
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time")
    private Date createTime;
}
