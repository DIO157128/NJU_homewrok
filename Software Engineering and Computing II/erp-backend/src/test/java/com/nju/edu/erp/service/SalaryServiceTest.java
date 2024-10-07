package com.nju.edu.erp.service;

import com.nju.edu.erp.dao.PostDao;
import com.nju.edu.erp.dao.SalarySheetDao;
import com.nju.edu.erp.dao.StaffDao;
import com.nju.edu.erp.enums.Format;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.PostPO;
import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import com.nju.edu.erp.model.vo.PostVO;
import com.nju.edu.erp.model.vo.StaffVO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.service.SalaryService.SalaryService;
import com.nju.edu.erp.service.SalaryStrategy.CalStrategyContext;
import com.nju.edu.erp.service.StaffService.PunchInService;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SpringBootTest
class SalaryServiceTest {
    @Autowired
    SalarySheetDao salarySheetDao;

    @Autowired
    SalaryService salaryService;

    /**
     * 以下两条为制定工资策略时使用
     */
    @Autowired
    PunchInService punchInService;

    @Autowired
    SaleService saleService;

    @Autowired
    CalStrategyContext context;

    @Autowired
    StaffService staffService;

    @Autowired
    UserService userService;

    @Autowired
    PostDao postDao;

    @Autowired
    StaffDao staffDao;

    @Test
    @Transactional
    void makeSalarySheet_1Test() {//测试普通工资单的制定（type为1）
        UserVO userVO = UserVO.builder()
                .name("caiwurenyuan")
                .role(Role.FINANCIAL_STAFF)
                .build();
        SalarySheetVO salarySheetVO = SalarySheetVO.builder()
                .staffId(1)
                .staffName("seecoder")
                .cardAccount("12345")
                .dueSalary(BigDecimal.valueOf(123))
                .tax(BigDecimal.valueOf(23))
                .actualSalary(BigDecimal.valueOf(100))
                .type(1)
                .build();
        SalarySheetPO prevSheet = salarySheetDao.getLatestSheet();
        String realSheetId = IdGenerator.generateSheetId(prevSheet == null ? null : prevSheet.getId(), "GZD");
        salaryService.makeSalarySheet(userVO, salarySheetVO,SalarySheetState.PENDING_LEVEL_1);
        SalarySheetPO latestSheet = salarySheetDao.getLatestSheet();
        Assertions.assertNotNull(latestSheet);
        Assertions.assertEquals(realSheetId, latestSheet.getId());
        Assertions.assertEquals(SalarySheetState.PENDING_LEVEL_1, latestSheet.getState());
        String sheetId = latestSheet.getId();
        Assertions.assertNotNull(sheetId);
    }



    @Test
    @Transactional
    void getSalarySheetByStateTest() {
        List<SalarySheetVO> salarySheetByState = salaryService.getSalarySheetByState(SalarySheetState.SUCCESS);
        Assertions.assertNotNull(salarySheetByState);
        Assertions.assertNotEquals(0, salarySheetByState.size());
        SalarySheetVO sheet1 = salarySheetByState.get(0);
        Assertions.assertNotNull(sheet1);
        Assertions.assertEquals("GZD-20220630-00000", sheet1.getId());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_exceptions_1Test() { // 一级审批不能直接到审批完成 (提示：可以以抛出异常的方式终止流程，这样就能触发事务回滚)
        try {
            salaryService.approval("GZD-20220706-00015", SalarySheetState.SUCCESS);
        } catch (Exception ignore){
        } finally {
            SalarySheetPO sheet = salarySheetDao.findSheetById("GZD-20220706-00015");
            Assertions.assertEquals(SalarySheetState.PENDING_LEVEL_1,sheet.getState());
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_exceptions_2Test() { // 二级审批不能回到一级审批
        try {
            salaryService.approval("GZD-20220630-00002", SalarySheetState.PENDING_LEVEL_1);
        } catch (Exception ignore){
        } finally {
            SalarySheetPO sheet = salarySheetDao.findSheetById("GZD-20220630-00002");
            Assertions.assertEquals(SalarySheetState.PENDING_LEVEL_2,sheet.getState());
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_failedTest() { // 测试审批失败
        salaryService.approval("GZD-20220630-00001", SalarySheetState.FAILURE);
        SalarySheetPO sheet = salarySheetDao.findSheetById("GZD-20220630-00001");
        Assertions.assertEquals(SalarySheetState.FAILURE,sheet.getState());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_1Test() { // 测试一级审批
        salaryService.approval("GZD-20220630-00001", SalarySheetState.PENDING_LEVEL_2);
        SalarySheetPO sheet = salarySheetDao.findSheetById("GZD-20220630-00001");
        Assertions.assertEquals(SalarySheetState.PENDING_LEVEL_2,sheet.getState());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void approval_2Test() { // 测试二级审批
        salaryService.approval("GZD-20220630-00002", SalarySheetState.SUCCESS);
        SalarySheetPO sheet = salarySheetDao.findSheetById("GZD-20220630-00002");
        Assertions.assertEquals(SalarySheetState.SUCCESS,sheet.getState());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void generateSalaryTest() {                  // 测试生成工资单
        salarySheetDao.deleteAll();                 //需要删除本月记录，否则会跳过该员工
        List<SalarySheetVO> first = salaryService.generateSalary();
        Assertions.assertNotNull(first);
        Assertions.assertEquals(first.size(),8);

        UserVO userVO=UserVO.builder().name("HR").role(Role.HR).staffId(10).build();

        for (SalarySheetVO salarySheetVO : first) {
            salaryService.makeSalarySheet(userVO, salarySheetVO,SalarySheetState.PENDING_LEVEL_1);       //存入工资单
        }

        List<SalarySheetVO> second = salaryService.generateSalary();                            //第二次生成则不会产生结果
        Assertions.assertEquals(second.size(),0);
    }


    @Test
    @Transactional
    @Rollback(value = true)
    public void generateAnnualTest() {                  // 测试生成年终奖
        salarySheetDao.deleteAll();                 //需要删除本月记录，否则会跳过该员工
        List<SalarySheetVO> first = salaryService.generateAnnual();
        Assertions.assertNotNull(first);
        Assertions.assertEquals(first.size(),8);

        UserVO userVO=UserVO.builder().name("HR").role(Role.HR).staffId(10).build();

        for (SalarySheetVO salarySheetVO : first) {
            salaryService.makeSalarySheet(userVO, salarySheetVO,SalarySheetState.PENDING_LEVEL_1);       //存入工资单
        }

        List<SalarySheetVO> second = salaryService.generateAnnual();                            //第二次生成则不会产生结果
        Assertions.assertEquals(second.size(),0);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void strategy_MONTHLYTest() {                        // 测试生成工资-月薪制
        salarySheetDao.deleteAll();                         //需要删除本月记录，否则会跳过该员工

        //数据库中  kucun  的那个staff
        StaffPO staffPO= StaffPO.builder().id(3).postId(3).name("kucun").build();
        List<BigDecimal> first = context.generateSalary(staffPO);
        Assertions.assertEquals(first.get(0),BigDecimal.valueOf(0.0));         //因为没有打卡，所以本月工资为0

        UserVO userVO=UserVO.builder().role(Role.INVENTORY_MANAGER).staffId(3).name("kucun").build();
        punchInService.punchIn(userVO);                                      //伪造一次打卡，因此下面有一次打卡的工资

        salarySheetDao.deleteAll();
        List<BigDecimal> second = context.generateSalary(staffPO);

        /**
         * 工资：仅一天打卡上班，因此只有一天的工资
         * 税收：0.03的税收
         */
        assert(second.get(0).subtract(BigDecimal.valueOf((double) 5000 / 31)).doubleValue()<=0.001);
        assert (second.get(1).subtract(BigDecimal.valueOf((double) 5000/31*0.03)).doubleValue()<=0.001);
        assert (second.get(1).subtract(BigDecimal.valueOf((double) 5000/31*(1-0.03))).doubleValue()<=0.001);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void strategy_PERCENTAGETest() {                        // 测试生成工资-月薪制
        salarySheetDao.deleteAll();                            //需要删除本月记录，否则会跳过该员工

        //数据库中  kucun  的那个staff
        StaffPO staffPO= StaffPO.builder().id(8).postId(8).name("xiaoshoujingli").build();
        List<BigDecimal> first = context.generateSalary(staffPO);
        Assertions.assertEquals(first.get(0),BigDecimal.valueOf(0.0));         //因为没有打卡，所以本月工资为0

        UserVO userVO=UserVO.builder().role(Role.SALE_MANAGER).staffId(8).name("xiaoshoujingli").build();
        punchInService.punchIn(userVO);                                      //伪造一次打卡，因此下面有一次打卡的工资(包括提成)

        salarySheetDao.deleteAll();
        List<BigDecimal> second = context.generateSalary(staffPO);            //因为7月没有销售记录，因此没有销售提成

        /**
         * 工资：仅一天打卡上班，因此只有一天的工资
         * 税收：0.03的税收
         */
        assert(second.get(0).subtract(BigDecimal.valueOf((double) 5000 / 31)).doubleValue()<=0.001);
        assert (second.get(1).subtract(BigDecimal.valueOf((double) 5000/31*0.03)).doubleValue()<=0.001);
        assert (second.get(1).subtract(BigDecimal.valueOf((double) 5000/31*(1-0.03))).doubleValue()<=0.001);
    }


    @Test
    @Transactional
    @Rollback(value = true)
    public void strategy_YEARLYTest() {                        // 测试生成工资-年薪制
        salarySheetDao.deleteAll();                         //需要删除本年记录，否则会跳过该员工

        //数据库中  67  的那个staff,GM
        StaffPO staffPO= StaffPO.builder().id(5).postId(5).name("67").build();
        List<BigDecimal> first = context.generateSalary(staffPO);

        /**
         *  10000*12=120000
         *  税分段计算得出38840
         */
        assert(first.get(0).subtract(BigDecimal.valueOf(120000)).doubleValue()<=0.001);
        assert (first.get(1).subtract(BigDecimal.valueOf(38840)).doubleValue()<=0.001);
        assert (first.get(1).subtract(BigDecimal.valueOf(81160)).doubleValue()<=0.001);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void strategy_MONTHLY_ANNTest() {                            // 测试生成年终奖-月薪制
        salarySheetDao.deleteAll();                                 //需要删除记录，否则会跳过该员工

        //数据库中  kucun  的那个staff
        StaffPO staffPO= StaffPO.builder().id(3).postId(3).name("kucun").build();
        List<BigDecimal> first = context.generateAnnual(staffPO);

        /**
         * 今年有一天打卡
         */
        double sal=5000*(1+(double)1/365);
        double _tax=sal*0.1-210;                //10%的税再减去速算扣除数
        assert (first.get(0).subtract(BigDecimal.valueOf(sal)).doubleValue()<=0.01);
        assert (first.get(1).subtract(BigDecimal.valueOf(_tax)).doubleValue()<=0.01);
        assert (first.get(2).subtract(BigDecimal.valueOf(sal-_tax)).doubleValue()<=0.01);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void strategy_PERCENTAGE_ANNTest() {                            // 测试生成年终奖-提成制
        salarySheetDao.deleteAll();                                 //需要删除记录，否则会跳过该员工

        //数据库中  xiaoshoujingli  的那个staff
        StaffPO staffPO= StaffPO.builder().id(8).postId(8).name("xiaoshoujingli").build();
        List<BigDecimal> first = context.generateAnnual(staffPO);

        /**
         * 销售经理今年打卡一次，同时销售额达到8608700，奖励年终奖 86块 ，故年终奖为 5000*（1+1/365）+ 86.087，据此计算税款即可
         */
        assert(first.get(0).subtract(BigDecimal.valueOf(5100)).doubleValue()<=0.1);
        assert(first.get(1).subtract(BigDecimal.valueOf(300)).doubleValue()<=0.1);
        assert(first.get(2).subtract(BigDecimal.valueOf(4800)).doubleValue()<=0.1);
    }


    @Test
    @Transactional
    @Rollback(value = true)
    public void strategy_YEARLY_ANNTest() {                            // 测试生成年终奖-提成制
        salarySheetDao.deleteAll();                                 //需要删除记录，否则会跳过该员工

        StaffPO staffPO= StaffPO.builder().id(5).postId(5).name("67").build();
        List<BigDecimal> first = context.generateAnnual(staffPO);

        /**
         * 总经理2w的年终奖，税收20000*20%-1410=2590
         */
        assert(first.get(0).subtract(BigDecimal.valueOf(20000)).doubleValue()<=0.1);
        assert(first.get(1).subtract(BigDecimal.valueOf(2590)).doubleValue()<=0.1);
        assert(first.get(2).subtract(BigDecimal.valueOf(17410)).doubleValue()<=0.1);
    }


    @Test
    @Transactional
    @Rollback(value = true)
    public void salaryFlowTest() {
        salarySheetDao.deleteAll();                                 //需要删除记录，否则会跳过该员工

        List<SalarySheetVO> VOS = salaryService.generateSalary();
        for (SalarySheetVO s : VOS) {
            salaryService.makeSalarySheet(new UserVO(), s,SalarySheetState.PENDING_LEVEL_1);       //存入工资单
        }

        Assertions.assertTrue(VOS.size()>=1);

        String id = salarySheetDao.findMaxId();
        SalarySheetPO PO0 = salarySheetDao.findSheetById(id);
        Assertions.assertEquals(PO0.getState(),SalarySheetState.PENDING_LEVEL_1);

        salaryService.approval(id,SalarySheetState.PENDING_LEVEL_2);
        SalarySheetPO PO1 = salarySheetDao.findSheetById(id);
        Assertions.assertEquals(PO1.getState(),SalarySheetState.PENDING_LEVEL_2);

        salaryService.approval(id,SalarySheetState.SUCCESS);
        SalarySheetPO PO3 = salarySheetDao.findSheetById(id);
        Assertions.assertEquals(PO3.getState(),SalarySheetState.SUCCESS);
    }
    /**
     * 集成测试
     * 创建岗位(PASS)- 创建员工 - 获取工资 - 创建用户 - 打卡 - 二次打卡 - 获取工资
     */
    @Test
    @Transactional
    @Rollback(value = true)
    public void staffAndSalaryTest() {                              // 测试生成工资-月薪制
        salarySheetDao.deleteAll();                             //需要删除本月记录，否则会跳过该员工

        //数据库中  kucun  的那个staff
        PostVO postVO=PostVO.builder().postName("SALE_MANAGER").postSalary(11000).basicSalary(11000).level(2).calSalaryMethod("PERCENTAGE").paySalaryMethod("CARD").taxDeductionRemark("无").build();
        Format f = staffService.addPost(postVO);
        Assertions.assertEquals(f,Format.PASS);

        int postId = postDao.getMaxId();
        int ifHasPost = postDao.findIfHasPost(postId);
        assert (ifHasPost==1);
        PostPO po = postDao.findByPostId(postId);
        Assertions.assertNotNull(po);
        Assertions.assertEquals(po.getCalSalaryMethod(),"PERCENTAGE");

        StaffVO staffVO= StaffVO.builder().id(9).postId(postId).birthday("2002-04-01 00:00:00").name("jichengceshi").phoneNum("18924002827").gender(0).cardAccountId("00022").build();
        Format f2 = staffService.addStaff(staffVO);
        int staffId = staffDao.getMaxId();
        Assertions.assertEquals(f2,Format.PASS);

        StaffPO staffPO= StaffPO.builder().id(staffId).postId(postId).name("jichengceshi").build();
        List<BigDecimal> first = context.generateSalary(staffPO);
        Assertions.assertEquals(first.get(0),BigDecimal.valueOf(0.0));         //因为没有打卡，所以本月工资为0

        UserVO userVO=UserVO.builder().role(Role.SALE_MANAGER).staffId(staffId).name("jichengceshi").password("jichengceshi").build();
        userService.register(userVO);
        boolean fir = punchInService.punchIn(userVO);                          //伪造一次打卡，因此下面有一次打卡的工资(包括提成)
        Assertions.assertTrue(fir);

        boolean sec = punchInService.punchIn(userVO);
        Assertions.assertFalse(sec);                                            //第二次打卡失败

        salarySheetDao.deleteAll();
        List<BigDecimal> second = context.generateSalary(staffPO);            //因为7月没有销售记录，因此没有销售提成

        /**
         * 工资：仅一天打卡上班，因此只有一天的工资
         * 税收：0.03的税收
         */
        assert(second.get(0).subtract(BigDecimal.valueOf((double) 11000 / 31)).doubleValue()<=0.001);
        assert (second.get(1).subtract(BigDecimal.valueOf((double) 11000/31*0.03)).doubleValue()<=0.001);
        assert (second.get(2).subtract(BigDecimal.valueOf((double) 11000/31*(1-0.03))).doubleValue()<=0.001);
    }
}
