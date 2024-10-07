package com.nju.edu.erp.enums.sheetState;

import com.nju.edu.erp.enums.BaseEnum;

public enum PaySheetState implements BaseEnum<PaySheetState, String> {


    SUCCESS("审批完成"),
    FAILURE("审批失败"),
    PENDING("待审批");

    private final String value;

    PaySheetState(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
