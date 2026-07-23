package org.example.controller;

import jakarta.validation.Valid;
import org.example.exception.ServiceException;
import org.example.pojo.Result;
import org.example.pojo.entity.Bill;
import org.example.service.billService;
import org.example.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账单接口
 */
@RestController
@RequestMapping("/bill")
public class billController {
    @Autowired
    private billService billService;

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 添加账单
     */
    @PostMapping
    public Result<Bill> addBill(@Valid @RequestBody Bill bill) {
        if (bill.getBillImg() != null && !bill.getBillImg().isEmpty()) {
            String url;
            try {
                url = aliOssUtil.uploadFile(bill.getBillImg(), "");
            } catch (IllegalArgumentException e) {
                throw new ServiceException(400, e.getMessage());
            }
            bill.setBillImg(url);
        }
        billService.addBill(bill);
        return Result.success();
    }

}
