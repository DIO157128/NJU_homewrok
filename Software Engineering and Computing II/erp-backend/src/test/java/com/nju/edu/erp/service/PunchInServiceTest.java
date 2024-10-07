package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.punchIn.PunchInRecordContentVO;
import com.nju.edu.erp.model.vo.punchIn.PunchInRecordVO;
import com.nju.edu.erp.service.StaffService.PunchInService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class PunchInServiceTest {

    @Autowired
    PunchInService punchInService;


    @Test
    @Transactional
    @Rollback
    public void punchInTest(){
        UserVO userVO = UserVO.builder()
                .name("xiaoshoujingli")
                .role(Role.SALE_MANAGER)
                .staffId(8)
                .build();
        boolean first = punchInService.punchIn(userVO);
        assertTrue(first);
        List<PunchInRecordContentVO> VOS = punchInService.findPunchInRecordContentByStaffAndPeriod(8, "2022-07-01 23:17:40", "2022-07-30 23:17:40");
        assertNotNull(VOS);
        assertEquals(1,VOS.size());
    }

    /**
     * 重复打卡
     */
    @Test
    @Transactional
    @Rollback
    public void punchIn_duplicateTest(){
        UserVO userVO = UserVO.builder()
                .name("xiaoshoujingli")
                .role(Role.SALE_MANAGER)
                .staffId(8)
                .build();
        boolean first = punchInService.punchIn(userVO);
        assertTrue(first);
        List<PunchInRecordContentVO> VOS = punchInService.findPunchInRecordContentByStaffAndPeriod(8, "2022-07-01 23:17:40", "2022-07-30 23:17:40");
        assertNotNull(VOS);
        assertEquals(1,VOS.size());

        //第二次打卡失败，数据也不会存下来
        boolean second = punchInService.punchIn(userVO);
        assertFalse(second);
        List<PunchInRecordContentVO> VOS_second = punchInService.findPunchInRecordContentByStaffAndPeriod(8, "2022-07-01 23:17:40", "2022-07-30 23:17:40");
        assertNotNull(VOS_second);
        assertEquals(1,VOS_second.size());

    }

    @Test
    @Transactional
    @Rollback
    public void findAllPunchInRecordByPeriodTest() throws ParseException {
        List<PunchInRecordVO> records = punchInService.findAllPunchInRecordByPeriod("2022-05-29 23:17:40", "2022-06-01 23:17:40");
        Assertions.assertNotNull(records);
        Assertions.assertEquals(8,records.size());
    }

    @Test
    @Transactional
    @Rollback
    public void findPunchInRecordContentByStaffAndPeriodTest() throws ParseException {
        UserVO userVO = UserVO.builder()
                .name("xiaoshoujingli")
                .role(Role.SALE_MANAGER)
                .staffId(8)
                .build();

        List<PunchInRecordContentVO> recordContents = punchInService.findPunchInRecordContentByStaffAndPeriod(8, "2022-05-28 23:17:40", "2022-06-06 23:17:40");
        Assertions.assertNotNull(recordContents);
        Assertions.assertEquals(1,recordContents.size());

    }




}
