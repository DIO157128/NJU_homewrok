package com.nju.edu.erp.service.BusinessService;

import com.nju.edu.erp.model.po.BusinessSituationPO;
import com.nju.edu.erp.model.vo.BusinessHistoryVO;
import com.nju.edu.erp.model.vo.BusinessSituationVO;
import com.nju.edu.erp.model.vo.SaleDetailVO;

import java.util.Date;
import java.util.List;

public interface BusinessService {
    /**
     * 获得销售明细
     *
     * @return 创建的分类信息
     */
    List<SaleDetailVO> getSaleDetail();

    /**
     * 获取经营情况表
     * @return
     */
    BusinessSituationVO getBusinessSituation(Date fromDate, Date toDate);

    /**
     * 获取经营历程表
     */
    BusinessHistoryVO getBusinessHistory();
}
