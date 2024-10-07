package com.nju.edu.erp.model.vo;

import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.pay.PaySheetVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnsSheetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessHistoryVO {
    /**
     * 销售单列表
     */
    private List<SaleSheetVO> saleList;
    /**
     * 销售退货单列表
     */
    private List<SaleReturnsSheetVO> saleReturnList;
    /**
     * 进货单列表
     */
    private List<PurchaseSheetVO> purchaseList;
    /**
     * 进货退货单列表
     */
    private List<PurchaseReturnsSheetVO> purchaseReturnList;
    /**
     * 付款单列表
     */
    private List<PaySheetVO> payList;
    /**
     * 收款单列表
     */
    private List<ReceiveSheetVO> receiveList;
    /**
     * 工资单列表
     */
    private List<SalarySheetVO> salaryList;
}
