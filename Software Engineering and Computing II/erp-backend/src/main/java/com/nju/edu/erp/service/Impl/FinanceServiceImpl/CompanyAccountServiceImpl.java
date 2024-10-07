package com.nju.edu.erp.service.Impl.FinanceServiceImpl;

import com.nju.edu.erp.dao.CompanyAccountDao;
import com.nju.edu.erp.model.po.CompanyAccountPO;
import com.nju.edu.erp.model.vo.CompanyAccountVO;
import com.nju.edu.erp.service.FinanceService.CompanyAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyAccountServiceImpl implements CompanyAccountService {


    private final CompanyAccountDao companyAccountDao;

    @Autowired
    public CompanyAccountServiceImpl(CompanyAccountDao companyAccountDao) {
        this.companyAccountDao = companyAccountDao;
    }

    @Override
    public int addCompanyAccount(CompanyAccountVO companyAccountVO) {
        CompanyAccountPO companyAccountPO = new CompanyAccountPO();
        BeanUtils.copyProperties(companyAccountVO, companyAccountPO);
        return companyAccountDao.addCompanyAccount(companyAccountPO);
    }

    @Override
    public int updateCompanyAccount(CompanyAccountVO companyAccountVO) {
        CompanyAccountPO companyAccountPO = new CompanyAccountPO();
        BeanUtils.copyProperties(companyAccountVO, companyAccountPO);
        return companyAccountDao.updateCompanyAccount(companyAccountPO);
    }

    @Override
    public int deleteCompanyAccount(Integer id) {
        return companyAccountDao.deleteCompanyAccount(id);
    }

    @Override
    public List<CompanyAccountVO> queryAllCompanyAccounts() {
        List<CompanyAccountVO> VOs = new ArrayList<>();
        List<CompanyAccountPO> POs = companyAccountDao.queryAllAccounts();
        for(CompanyAccountPO po : POs){
            CompanyAccountVO vo = new CompanyAccountVO();
            BeanUtils.copyProperties(po, vo);
            VOs.add(vo);
        }
        return VOs;
    }

    @Override
    public CompanyAccountVO findOneByName(String name) {
        CompanyAccountPO companyAccountPO = companyAccountDao.findOneByName(name);
        CompanyAccountVO companyAccountVO = new CompanyAccountVO();
        BeanUtils.copyProperties(companyAccountPO, companyAccountVO);
        return companyAccountVO;
    }


}
