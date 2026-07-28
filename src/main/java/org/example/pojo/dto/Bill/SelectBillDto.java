package org.example.pojo.dto.Bill;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author : XR
 * @date :2026/7/24 10:29
 * @description :TODO
 */
@Data
public class SelectBillDto {
    @NotNull(message = "用户id不能为空")
    @JsonProperty("user_id")
    private Long userId;

    @NotBlank(message = "查询月份不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}$",message = "日期格式为年-月")
    private String month;

    private Integer type;// 账单类型
    @JsonProperty("pay_type")
    private Integer payType;       // 支付方式
    @JsonProperty("category_id")
    private Integer categoryId;       // 用途分类ID
}
