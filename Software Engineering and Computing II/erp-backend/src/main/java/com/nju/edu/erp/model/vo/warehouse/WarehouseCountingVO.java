package com.nju.edu.erp.model.vo.warehouse;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.utils.PidConverter;
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
public class WarehouseCountingVO {
    /**
     * 库存id
     */
    @ExcelIgnore
    private Integer id;

    /**
     * 商品编号
     */
    @ExcelProperty(value="商品id",converter= PidConverter.class)
    private ProductInfoVO product;

    /**
     * 数量
     */
    @ExcelProperty("数量")
    private Integer quantity;

    /**
     * 进价
     */
    @ExcelProperty("价格")
    private BigDecimal purchasePrice;

    /**
     * 批次
     */
    @ExcelProperty("批次")
    private Integer batchId;

    /**
     * 出厂日期
     */
    @ExcelProperty("日期")
    private Date productionDate;
}
