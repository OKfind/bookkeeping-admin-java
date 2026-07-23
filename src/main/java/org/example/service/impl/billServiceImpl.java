package org.example.service.impl;

import org.example.exception.ServiceException;
import org.example.mapper.billMapper;
import org.example.pojo.entity.Bill;
import org.example.pojo.enums.BillPayType;
import org.example.service.billService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 账单业务实现
 */
@Service
public class billServiceImpl implements billService {
    @Autowired
    private billMapper billMapper;

    /**
     * 添加账单
     */
    @Override
    public void addBill(Bill bill) {
        if (bill == null) {
            throw new ServiceException(400, "账单信息不能为空");
        }
        if (bill.getUserId() == null) {
            throw new ServiceException(400, "用户id不能为空");
        }
        if (bill.getType() == null || (bill.getType() != 1 && bill.getType() != 2)) {
            throw new ServiceException(400, "账单类型只能是1或2");
        }
        if (!BillPayType.isValid(bill.getPayType())) {
            throw new ServiceException(400, BillPayType.LIMIT_MESSAGE);
        }
        if (bill.getAmount() == null || bill.getAmount() <= 0) {
            throw new ServiceException(400, "账单金额必须大于0");
        }
        if (bill.getCategoryId() == null) {
            throw new ServiceException(400, "账单类型id不能为空");
        }
        if (bill.getBillTime() == null) {
            throw new ServiceException(400, "账单发生时间不能为空");
        }

        bill.setId(null);
        if (bill.getCreateTime() == null) {
            bill.setCreateTime(new Date());
        }

        billMapper.insert(bill);
    }
}
