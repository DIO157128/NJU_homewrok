package com.nju.edu.erp.service.FinanceService;

import com.nju.edu.erp.model.vo.CompanyAccountVO;

import java.util.List;

public interface CompanyAccountService {
    /**
     * 创建新的公司账户
     */
    int addCompanyAccount(CompanyAccountVO companyAccountVO);

    /**
     * 更新公司账户
     */
    int updateCompanyAccount(CompanyAccountVO companyAccountVO);

    /**
     * 删除公司扎账户
     */
    int deleteCompanyAccount(Integer id);

    /**
     * 返回公司所有账户
     * @return
     */
    List<CompanyAccountVO> queryAllCompanyAccounts();

    /**
     * 根据名称返回账户
     * @param name
     * @return
     */
    CompanyAccountVO findOneByName(String name);
}
