package com.nju.edu.erp.service.StaffService;

import com.nju.edu.erp.model.po.punchIn.PunchInRecordContentPO;
import com.nju.edu.erp.model.vo.punchIn.PunchInRecordContentVO;
import com.nju.edu.erp.model.vo.punchIn.PunchInRecordVO;
import com.nju.edu.erp.model.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface PunchInService {


    /**
     *      员工打卡
     * @param user
     */
    boolean punchIn(UserVO user);


    /**
     *  获取所有的打卡记录，如 传入 2022 年 6 月 1 日         2022 年 6 月 30 日
     *      id          姓名          次数
     *      003         张三          28
     *      004         李四          27
     *      004         王五          29
     *
     * @return
     */
    List<PunchInRecordVO> findAllPunchInRecordByPeriod(String beginDateStr,String endDateStr);


    /**
     *  获取某位的打卡记录，如 传入UserVO，时间 2022 年 6 月 1 日         2022 年 6 月 30 日
     *          id          姓名          时间
     *          003         张三          6月2日
     *          003         张三          6月3日
     *          003         张三          6月4日
     * @return
     */
    List<PunchInRecordContentVO> findPunchInRecordContentByStaffAndPeriod(int staffId, String beginDateStr, String endDateStr);


    int countByIdAndPeriod(int id, Date t1,Date t2);
}
