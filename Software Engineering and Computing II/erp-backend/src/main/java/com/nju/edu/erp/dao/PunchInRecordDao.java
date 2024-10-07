package com.nju.edu.erp.dao;


import com.nju.edu.erp.model.po.punchIn.PunchInRecordContentPO;
import com.nju.edu.erp.model.po.punchIn.PunchInRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface PunchInRecordDao {

    /**
     *
     * @param staffId
     * @param punchInTime
     * @return 影响的行数
     */
    int punchIn(int staffId,String staffName, Date punchInTime);


    /**
     *              检查今天有没有打过卡
     * @param staffId
     * @param punchInTime
     * @return
     */
    int checkPunchIn(int staffId,Date punchInTime);

    /**
     *          根据某人某时间段进行查找
     * @return
     */
    List<PunchInRecordContentPO> findByIdAndPeriod(int staffId, Date t1, Date t2);

    /**
     *          计数，制定工资单的时候会用到
     * @param staffId
     * @param t1
     * @param t2
     * @return
     */
    int countByIdAndPeriod(int staffId,Date t1,Date t2);

    /**
     *  根据时间获取所有员工的打卡记录
     * @return
     */
    List<PunchInRecordPO> findAllRecord(Date t1, Date t2);

}
