package org.example.vo;

import lombok.Data;

import java.util.List;

/**
 * @author : XR
 * @date :2026/6/25 11:37
 * @description :TODO
 */
@Data
public class pageMenuVo {
    private Integer id;
    private String menuName;
    private Integer sort;
    private Integer disabled;
    private List<pageMenuVo> children;
}
