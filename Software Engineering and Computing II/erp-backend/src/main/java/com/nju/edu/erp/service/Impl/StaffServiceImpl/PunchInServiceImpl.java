package com.nju.edu.erp.service.Impl.StaffServiceImpl;

import com.nju.edu.erp.dao.PunchInRecordDao;
import com.nju.edu.erp.model.po.punchIn.PunchInRecordContentPO;
import com.nju.edu.erp.model.po.punchIn.PunchInRecordPO;
import com.nju.edu.erp.model.vo.punchIn.PunchInRecordContentVO;
import com.nju.edu.erp.model.vo.punchIn.PunchInRecordVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.service.StaffService.PunchInService;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.service.UserService;
import com.nju.edu.erp.utils.TimeFormatConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PunchInServiceImpl implements PunchInService {


    private final PunchInRecordDao punchInRecordDao;

    private final StaffService staffService;

    private final UserService userService;



    @Autowired
    public PunchInServiceImpl(PunchInRecordDao punchInRecordDao, StaffService staffService, UserService userService) {
        this.punchInRecordDao = punchInRecordDao;
        this.staffService = staffService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public boolean punchIn(UserVO user) {
        int staffId = userService.findByUsername(user.getName()).getStaffId();
        if (staffService.findByStaffId(staffId) == null) return false;
        int effectLine = punchInRecordDao.checkPunchIn(staffId, new Date());
        if (effectLine != 0) return false;
        String staffName = staffService.findByStaffId(staffId).getName();
        int effectLines = punchInRecordDao.punchIn(staffId, staffName, new Date());
        if (effectLines == 0) throw new RuntimeException("状态更新失败");
        return true;
    }

    @Override
    @Transactional
    public List<PunchInRecordVO> findAllPunchInRecordByPeriod(String beginDateStr, String endDateStr) {
        if (TimeFormatConverter.checkTimeError(beginDateStr, endDateStr)) return null;
        List<PunchInRecordPO> allRecordPOs = punchInRecordDao.findAllRecord(TimeFormatConverter.timeTransfer(beginDateStr), TimeFormatConverter.timeTransfer(endDateStr));

        //以下几步皆为从POs转为VOs
        List<PunchInRecordVO> recordVOs = new ArrayList<>();
        for (PunchInRecordPO allRecordPO : allRecordPOs) {
            PunchInRecordVO tmp = new PunchInRecordVO();
            BeanUtils.copyProperties(allRecordPO, tmp);
            recordVOs.add(tmp);
        }
        return recordVOs;
    }

    @Override
    @Transactional
    public List<PunchInRecordContentVO> findPunchInRecordContentByStaffAndPeriod(int staffId, String beginDateStr, String endDateStr) {
        if (TimeFormatConverter.checkTimeError(beginDateStr, endDateStr)) return null;
        List<PunchInRecordContentPO> recordsPOs = punchInRecordDao.findByIdAndPeriod(staffId, TimeFormatConverter.timeTransfer(beginDateStr), TimeFormatConverter.timeTransfer(endDateStr));

        //以下几步皆为从POs转为VOs
        List<PunchInRecordContentVO> recordsVOs = new ArrayList<>();
        PunchInRecordContentVO tmp = new PunchInRecordContentVO();
        for (PunchInRecordContentPO recordsPO : recordsPOs) {
            BeanUtils.copyProperties(recordsPO, tmp);
            recordsVOs.add(tmp);
        }
        return recordsVOs;
    }

    @Override
    public int countByIdAndPeriod(int id, Date t1, Date t2) {
        return punchInRecordDao.countByIdAndPeriod(id, t1, t2);
    }


}
