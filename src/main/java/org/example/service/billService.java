package org.example.service;

import org.example.pojo.dto.Bill.SelectBillDto;
import org.example.pojo.entity.Bill;
import org.example.vo.BillVO;

import java.util.List;

public interface billService {
    // 添加账单
    void addBill(Bill bill);

    // 查询当前用户的所有流水情况
    List<BillVO> selectAllBill(SelectBillDto selectBillDto);

    // 编辑账单
    void UpdateBill(Bill bill);

    // 删除账单
    void DeleteBill(Integer id);
}
