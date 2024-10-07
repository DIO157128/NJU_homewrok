package com.nju.edu.erp.service.Impl.FinanceServiceImpl;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.sheetState.PaySheetState;
import com.nju.edu.erp.model.po.CompanyAccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.pay.PaySheetContentPO;
import com.nju.edu.erp.model.po.pay.PaySheetPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.pay.PaySheetContentVO;
import com.nju.edu.erp.model.vo.pay.PaySheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.FinanceService.PayService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PayServiceImpl implements PayService {

    private final PaySheetDao paySheetDao;

    private final CustomerService customerService;

    private final CompanyAccountDao companyAccountDao;

    @Autowired
    public PayServiceImpl(PaySheetDao paySheetDao, CustomerService customerService, CompanyAccountDao companyAccountDao) {

        this.paySheetDao = paySheetDao;
        this.customerService = customerService;
        this.companyAccountDao = companyAccountDao;
    }

    @Override
    @Transactional
    public void makePaySheet(UserVO userVO, PaySheetVO paySheetVO) {
        PaySheetPO paySheetPO = new PaySheetPO();
        BeanUtils.copyProperties(paySheetVO, paySheetPO);

        paySheetPO.setOperator(userVO.getName());
        paySheetPO.setCreateDate(new Date());
        PaySheetPO latest = paySheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "FKD");
        paySheetPO.setId(id);
        paySheetPO.setState(PaySheetState.PENDING);
        BigDecimal allAmount = BigDecimal.ZERO;

        List<PaySheetContentPO> pContentPOList = new ArrayList<>();//创建条目清单
        for (PaySheetContentVO content : paySheetVO.getPaySheetContent()) {
            PaySheetContentPO pContentPO = new PaySheetContentPO();
            BeanUtils.copyProperties(content, pContentPO);
            pContentPO.setPaySheetId(id);
            pContentPOList.add(pContentPO);
            allAmount = allAmount.add(pContentPO.getTransferAmount());
        }

        paySheetDao.saveBatchSheetContent(pContentPOList);
        paySheetPO.setAllAmount(allAmount);
        paySheetDao.saveSheet(paySheetPO);
    }

    @Override
    @Transactional
    public List<PaySheetVO> getPaySheetByState(PaySheetState state) {
        List<PaySheetVO> res = new ArrayList<>();
        List<PaySheetPO> all;
        if (state == null) {
            all = paySheetDao.findAllSheet();
        } else {
            all = paySheetDao.findAllSheetByState(state);
        }
        for (PaySheetPO po : all) {
            PaySheetVO vo = new PaySheetVO();
            BeanUtils.copyProperties(po, vo);
            List<PaySheetContentPO> alll = paySheetDao.findContentBySheetId(po.getId());
            List<PaySheetContentVO> vos = new ArrayList<>();
            for (PaySheetContentPO p : alll) {
                PaySheetContentVO v = new PaySheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setPaySheetContent(vos);
            res.add(vo);
        }

        return res;


    }

    @Override
    @Transactional
    public void approval(String paySheetId, PaySheetState state) {
        //首先检查要更改的单据状态是否合法
        if (state.equals(PaySheetState.FAILURE)) {
            PaySheetPO paySheet = paySheetDao.findSheetById(paySheetId);
            if (paySheet.getState() == PaySheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = paySheetDao.updateSheetState(paySheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            PaySheetState prevState;
            if (state.equals(PaySheetState.SUCCESS)) {
                prevState = PaySheetState.PENDING;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = paySheetDao.updateSheetStateOnPrev(paySheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            PaySheetPO updatePO = paySheetDao.findSheetById(paySheetId);
            BigDecimal allAmount = updatePO.getAllAmount();
            String accountToUpdate = updatePO.getBankAccount();
            Integer payerId = updatePO.getPayerId();
            //状态更新成功后修改：
            //1、company_account的余额
            CompanyAccountPO companyAccountPO = companyAccountDao.findOneByName(accountToUpdate);
            BigDecimal res = companyAccountPO.getBalance().subtract(allAmount);
            if (res.compareTo(BigDecimal.ZERO) < 0) throw new RuntimeException("付款账户余额不足");
            companyAccountPO.setBalance(res);
            companyAccountDao.updateCompanyAccount(companyAccountPO);
            //2、customer的应付
            CustomerPO customerPO = customerService.findCustomerById(payerId);
            customerPO.setReceivable(customerPO.getPayable().subtract(allAmount));
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customerPO, customerVO);
            customerService.updateCustomer(customerVO);
        }
    }

    @Override
    @Transactional
    public PaySheetVO getPaySheetById(String paySheetId) {
        PaySheetPO paySheetPO = paySheetDao.findSheetById(paySheetId);
        if (paySheetPO == null) return null;
        List<PaySheetContentPO> contentPO = paySheetDao.findContentBySheetId(paySheetId);
        PaySheetVO pVO = new PaySheetVO();
        BeanUtils.copyProperties(paySheetPO, pVO);
        List<PaySheetContentVO> paySheetContentVOList = new ArrayList<>();
        for (PaySheetContentPO content : contentPO) {
            PaySheetContentVO pContentVO = new PaySheetContentVO();
            BeanUtils.copyProperties(content, pContentVO);
            paySheetContentVOList.add(pContentVO);
        }
        pVO.setPaySheetContent(paySheetContentVOList);
        return pVO;
    }

    @Override
    @Transactional
    public List<PaySheetPO> findAllSheetByDate(Date fromDate, Date toDate) {
        return paySheetDao.findAllSheetByDate(fromDate, toDate);
    }
}
