package org.example.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author : XR
 * @date :2026/7/24 17:15
 * @description :TODO
 */
@Data
public class BillVO {
    // bill 表的基础字段
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    private Integer type;
    @JsonProperty("pay_type")
    private Integer payType;
    @JsonProperty("bill_img")
    private String billImg;
    private Double amount;
    @JsonProperty("category_id")
    private Long categoryId;
    // 联表查出的 category 表字段
    @JsonProperty("category_name")
    private String categoryName;
    private String remark;
    @JsonProperty("bill_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date billTime;
    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
