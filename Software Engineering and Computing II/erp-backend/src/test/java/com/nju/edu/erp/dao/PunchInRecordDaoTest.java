package com.nju.edu.erp.dao;


import com.nju.edu.erp.model.po.punchIn.PunchInRecordContentPO;
import com.nju.edu.erp.model.po.punchIn.PunchInRecordPO;
import com.nju.edu.erp.utils.TimeFormatConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PunchInRecordDaoTest {
    @Autowired
    PunchInRecordDao punchInRecordDao;

    /**
     * 不能重复打卡的测试只能在service层测试
     */
    @Test
    @Transactional
    @Rollback(value = true)
    void punchInTest() {
        int first = punchInRecordDao.punchIn(3, "kucun", new Date());
        assertEquals(1,first);
    }

    /**
     * 测试有无重复打卡
     */
    @Test
    @Transactional
    @Rollback(value = true)
    void checkPunchInTest() {
        int checkPunchIn = punchInRecordDao.checkPunchIn(3, new Date());
        assertEquals(0,checkPunchIn);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findByIdAndPeriodTest() {
        List<PunchInRecordContentPO> POS = punchInRecordDao.findByIdAndPeriod(3, TimeFormatConverter.timeTransfer("2010-10-10 00:00:00"), new Date());
        assertNotNull(POS);
        assertEquals(1,POS.size());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void countByIdAndPeriodTest() {
        int cnt = punchInRecordDao.countByIdAndPeriod(3, TimeFormatConverter.timeTransfer("2010-10-10 00:00:00"), new Date());
        assertEquals(1,cnt);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findAllRecordTest() {
        List<PunchInRecordPO> allRecord = punchInRecordDao.findAllRecord(TimeFormatConverter.timeTransfer("2010-10-10 00:00:00"), new Date());

        assertEquals(8,allRecord.size());
    }
}
