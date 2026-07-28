package org.example.service.impl;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.example.exception.ServiceException;
import org.example.mapper.billMapper;
import org.example.pojo.dto.Bill.SelectBillDto;
import org.example.pojo.entity.Bill;
import org.example.pojo.entity.Category;
import org.example.pojo.enums.BillPayType;
import org.example.service.billService;
import org.example.vo.BillVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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

    /**
     * 查询当前用户在指定月份的所有账单流水情况
     * @param selectBillDto
     * @return
     */
    @Override
    public List<BillVO> selectAllBill(SelectBillDto selectBillDto) {
        // 1、解析传递过来的日期字符串
        YearMonth yearMonth = YearMonth.parse(selectBillDto.getMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));

        // 2、计算该月的起始时间（1号 00：00：00）
        LocalDateTime startTime = yearMonth.atDay(1).atStartOfDay();

        // 3、计算该月的结束时间（最后一天 23：59：59）
        LocalDateTime endTime = yearMonth.atEndOfMonth().atTime(23,59,59);

        // 4、查询sql
        MPJLambdaWrapper<Bill> wrapper = new MPJLambdaWrapper<>();
        // 必传条件 userId和month
        wrapper.selectAll(Bill.class)
                .selectAs(Category::getName, BillVO::getCategoryName)
                .leftJoin(Category.class, Category::getId, Bill::getCategoryId)
                .eq(Bill::getUserId,selectBillDto.getUserId())
                .ge(Bill::getBillTime, java.sql.Timestamp.valueOf(startTime))
                .le(Bill::getBillTime, java.sql.Timestamp.valueOf(endTime));

        // 不是必传条件
        wrapper.eq(selectBillDto.getType() !=null,Bill::getType, selectBillDto.getType())
                .eq(selectBillDto.getPayType() !=null,Bill::getPayType,selectBillDto.getPayType())
                .eq(selectBillDto.getCategoryId() !=null,Bill::getCategoryId,selectBillDto.getCategoryId())
                .orderByDesc(Bill::getBillTime);
        return billMapper.selectJoinList(BillVO.class, wrapper);
    }

    /**
     * 编辑账单
     * @param bill
     */
    @Override
    public void UpdateBill(Bill bill) {
        if(bill.getId() == null){
            throw new ServiceException(400, "账单id为空");
        }
        billMapper.updateById(bill);
    }

    /**
     * 删除账单
     * @param id
     */
    @Override
    public void DeleteBill(Integer id) {
        if(id == null){
            throw new ServiceException(400, "账单id为空");
        }
        billMapper.deleteById(id);
    }
}
