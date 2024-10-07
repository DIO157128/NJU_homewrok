package com.nju.edu.erp.enums.sheetState;

import com.nju.edu.erp.enums.BaseEnum;

public enum SalarySheetState implements BaseEnum<SalarySheetState, String> {


    SUCCESS("审批通过"),
    FAILURE("审批失败"),
    PENDING_LEVEL_1("待HR审批"),
    PENDING_LEVEL_2("待总经理审批");

    private final String value;

    SalarySheetState(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
