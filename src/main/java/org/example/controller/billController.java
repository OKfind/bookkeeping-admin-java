package org.example.controller;

import jakarta.validation.Valid;
import org.example.pojo.Result;
import org.example.pojo.entity.Bill;
import org.example.service.billService;
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

    /**
     * 添加账单
     */
    @PostMapping
    public Result<Bill> addBill(@Valid @RequestBody Bill bill) {
        billService.addBill(bill);
        return Result.success();
    }
}
