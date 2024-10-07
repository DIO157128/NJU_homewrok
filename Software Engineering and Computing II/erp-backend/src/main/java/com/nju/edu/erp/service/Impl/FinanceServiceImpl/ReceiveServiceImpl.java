package com.nju.edu.erp.service.Impl.FinanceServiceImpl;

import com.nju.edu.erp.dao.ReceiveSheetDao;
import com.nju.edu.erp.enums.sheetState.ReceiveSheetState;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetContentPO;
import com.nju.edu.erp.model.po.receive.ReceiveSheetPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetContentVO;
import com.nju.edu.erp.model.vo.receive.ReceiveSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.FinanceService.ReceiveService;
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
public class ReceiveServiceImpl implements ReceiveService {
    private final ReceiveSheetDao receiveSheetDao;

    private final CustomerService customerService;

    @Autowired
    public ReceiveServiceImpl(ReceiveSheetDao receiveSheetDao, CustomerService customerService) {

        this.receiveSheetDao = receiveSheetDao;
        this.customerService = customerService;
    }

    @Override
    @Transactional
    public void makeReceiveSheet(UserVO userVO, ReceiveSheetVO receiveSheetVO) {
        ReceiveSheetPO receiveSheetPO = new ReceiveSheetPO();
        BeanUtils.copyProperties(receiveSheetVO, receiveSheetPO);

        receiveSheetPO.setOperator(userVO.getName());
        receiveSheetPO.setCreateDate(new Date());
        ReceiveSheetPO latest = receiveSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "SKD");
        receiveSheetPO.setId(id);
        receiveSheetPO.setState(ReceiveSheetState.PENDING);
        BigDecimal allAmount = BigDecimal.ZERO;

        List<ReceiveSheetContentPO> rContentPOList = new ArrayList<>();//创建条目清单
        for (ReceiveSheetContentVO content : receiveSheetVO.getReceiveSheetContent()) {
            ReceiveSheetContentPO rContentPO = new ReceiveSheetContentPO();
            BeanUtils.copyProperties(content, rContentPO);
            rContentPO.setReceiveSheetId(id);
            rContentPOList.add(rContentPO);
            allAmount = allAmount.add(rContentPO.getTransferAmount());
        }
        receiveSheetDao.saveBatchSheetContent(rContentPOList);
        receiveSheetPO.setAllAmount(allAmount);
        receiveSheetPO.setActualAmount(allAmount.subtract(receiveSheetPO.getDiscount()));
        receiveSheetDao.saveSheet(receiveSheetPO);
    }

    @Override
    @Transactional
    public List<ReceiveSheetVO> getReceiveSheetByState(ReceiveSheetState state) {
        List<ReceiveSheetVO> res = new ArrayList<>();
        List<ReceiveSheetPO> all;
        if (state == null) {
            all = receiveSheetDao.findAllSheet();
        } else {
            all = receiveSheetDao.findAllSheetByState(state);
        }
        for (ReceiveSheetPO po : all) {
            ReceiveSheetVO vo = new ReceiveSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<ReceiveSheetContentPO> alll = receiveSheetDao.findContentBySheetId(po.getId());
            List<ReceiveSheetContentVO> vos = new ArrayList<>();
            for (ReceiveSheetContentPO p : alll) {
                ReceiveSheetContentVO v = new ReceiveSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setReceiveSheetContent(vos);
            res.add(vo);
        }

        return res;

    }

    @Override
    @Transactional
    public void approval(String receiveSheetId, ReceiveSheetState state) {
        //首先检查要更改的单据状态是否合法
        if (state.equals(ReceiveSheetState.FAILURE)) {
            ReceiveSheetPO receiveSheet = receiveSheetDao.findSheetById(receiveSheetId);
            if (receiveSheet.getState() == ReceiveSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = receiveSheetDao.updateSheetState(receiveSheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            ReceiveSheetState prevState;
            if (state.equals(ReceiveSheetState.SUCCESS)) {
                prevState = ReceiveSheetState.PENDING;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = receiveSheetDao.updateSheetStateOnPrev(receiveSheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            ReceiveSheetPO updatePO = receiveSheetDao.findSheetById(receiveSheetId);
            BigDecimal actualAmount = updatePO.getActualAmount();
            //customer的应收
            CustomerPO customerPO = customerService.findCustomerByName(updatePO.getCustomerName());
            customerPO.setReceivable(customerPO.getReceivable().subtract(actualAmount));
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customerPO, customerVO);
            customerService.updateCustomer(customerVO);
        }
    }

    @Override
    @Transactional
    public ReceiveSheetVO getReceiveSheetById(String receiveSheetId) {
        ReceiveSheetPO receiveSheetPO = receiveSheetDao.findSheetById(receiveSheetId);
        if (receiveSheetPO == null) return null;
        List<ReceiveSheetContentPO> contentPO = receiveSheetDao.findContentBySheetId(receiveSheetId);
        ReceiveSheetVO rVO = new ReceiveSheetVO();
        BeanUtils.copyProperties(receiveSheetPO, rVO);
        List<ReceiveSheetContentVO> receiveSheetContentVOList = new ArrayList<>();
        for (ReceiveSheetContentPO content : contentPO) {
            ReceiveSheetContentVO rContentVO = new ReceiveSheetContentVO();
            BeanUtils.copyProperties(content, rContentVO);
            receiveSheetContentVOList.add(rContentVO);
        }
        rVO.setReceiveSheetContent(receiveSheetContentVOList);
        return rVO;
    }

    @Override
    @Transactional
    public List<ReceiveSheetPO> findAllSheetByDate(Date fromDate, Date toDate) {
        return receiveSheetDao.findAllSheetByDate(fromDate, toDate);
    }
}
