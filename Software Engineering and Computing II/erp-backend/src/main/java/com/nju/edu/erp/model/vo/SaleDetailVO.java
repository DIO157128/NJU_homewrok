package com.nju.edu.erp.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetailVO {
    /**
     * 销售/退货时间
     */
    @ExcelProperty(value = "销售/退货时间")
    private Date createTime;
    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String pName;
    /**
     * 客户id
     */
    @ExcelProperty(value = "客户名称")
    private String sellerName;
    /**
     * 商品类别
     */
    @ExcelProperty(value = "商品类别")
    private String categoryType;
    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private int quantity;
    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal unit_price;
    /**
     * 总价
     */
    @ExcelProperty(value = "总价")
    private BigDecimal total_price;
}
