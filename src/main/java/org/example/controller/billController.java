package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.exception.ServiceException;
import org.example.pojo.Result;
import org.example.pojo.dto.Bill.SelectBillDto;
import org.example.pojo.entity.Bill;
import org.example.service.billService;
import org.example.utils.AliOssUtil;
import org.example.vo.BillVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 账单接口
 */
@RestController
@RequestMapping("/bill")
@Tag(name = "账单信息接口", description = "账单添加，统计基本信息等")
public class billController {
    @Autowired
    private billService billService;

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 添加账单
     */
    @Operation(summary = "添加账单")
    @PostMapping
    public Result addBill(@Valid @RequestBody Bill bill) {
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

    /**
     * 查询当前用户的流水状况
     * @param selectBillDto
     * @return
     */
    @Operation(summary = "查询当前用户的所有账单的流水状况")
    @GetMapping
    public Result<List<BillVO>> selectUserBills(@Valid @ModelAttribute SelectBillDto selectBillDto){
        List<BillVO> bills = billService.selectAllBill(selectBillDto);
        return Result.success(bills);
    }

    /**
     * 编辑当前用户账单的流水状况
     * @param bill
     * @return
     */
    @Operation(summary = "编辑当前用户账单的流水状况")
    @PutMapping
    public Result updateUserBills(@RequestBody Bill bill){
        billService.UpdateBill(bill);
        return Result.success();
    }

    /**
     * 删除用户账单
     * @param id
     * @return
     */
    @Operation(summary = "删除用户账单")
    @DeleteMapping("/{id}")
    public Result deleteUserBillByPath(@PathVariable Integer id){
        billService.DeleteBill(id);
        return Result.success();
    }
}
