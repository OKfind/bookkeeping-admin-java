package org.example.pojo.enums;

import lombok.Getter;

/**
 * 账单支付类型
 */
@Getter
public enum BillPayType {
    CASH(1, "现金"),
    WECHAT_PAY(2, "微信支付"),
    ALIPAY(3, "支付宝"),
    CREDIT_CARD(4, "信用卡"),
    DEBIT_CARD(5, "储蓄卡"),
    OTHER(6, "其它");

    public static final String LIMIT_MESSAGE = "支付类型只能是1：现金，2：微信支付，3：支付宝，4：信用卡，5：储蓄卡，6：其它";

    private final Integer code;
    private final String description;

    BillPayType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static boolean isValid(Integer code) {
        if (code == null) {
            return false;
        }
        for (BillPayType payType : values()) {
            if (payType.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
