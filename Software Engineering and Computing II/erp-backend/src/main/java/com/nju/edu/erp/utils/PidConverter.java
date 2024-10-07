package com.nju.edu.erp.utils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.nju.edu.erp.model.vo.ProductInfoVO;


public class PidConverter implements Converter<ProductInfoVO>{

    @Override
    public Class supportJavaTypeKey(){return ProductInfoVO.class;}

    @Override
    public CellDataTypeEnum supportExcelTypeKey(){return CellDataTypeEnum.STRING;}

    @Override
    public ProductInfoVO convertToJavaData(CellData cellData,ExcelContentProperty excelContentProperty,
                                           GlobalConfiguration globalConfiguration)
            throws Exception{
        ProductInfoVO productInfoVO=new ProductInfoVO();
        productInfoVO.setId(cellData.getStringValue());
        return productInfoVO;
    }

    @Override
    public CellData convertToExcelData(ProductInfoVO productInfoVO, ExcelContentProperty excelContentProperty,
                                       GlobalConfiguration globalConfiguration)
            throws Exception {
        return new CellData(productInfoVO.getId());
    }

}