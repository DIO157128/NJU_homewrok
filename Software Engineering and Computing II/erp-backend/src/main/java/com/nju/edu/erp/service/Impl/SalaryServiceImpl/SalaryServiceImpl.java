package com.nju.edu.erp.service.Impl.SalaryServiceImpl;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.sheetState.SalarySheetState;
import com.nju.edu.erp.model.po.StaffPO;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.salary.SalarySheetVO;
import com.nju.edu.erp.service.SalaryService.SalaryService;
import com.nju.edu.erp.service.SalaryStrategy.CalStrategyContext;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.service.StaffService.PunchInService;
import com.nju.edu.erp.service.StaffService.StaffService;
import com.nju.edu.erp.utils.TimeUtils;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SalaryServiceImpl implements SalaryService {

    private final SalarySheetDao salarySheetDao;

    private final StaffService staffService;

    private final SaleService saleService;

    private final PunchInService punchInService;

    private final CalStrategyContext context;


    @Autowired
    public SalaryServiceImpl(SalarySheetDao salarySheetDao, StaffService staffService, SaleService saleService, PunchInService punchInService, CalStrategyContext context) {
        this.salarySheetDao = salarySheetDao;
        this.staffService = staffService;
        this.saleService = saleService;
        this.punchInService = punchInService;
        this.context = context;
    }

    @Override
    @Transactional
    public void makeSalarySheet(UserVO userVO, SalarySheetVO salarySheetVO, SalarySheetState state) {

        int year = TimeUtils.getYear();

        SalarySheetPO salarySheetPO = new SalarySheetPO();
        BeanUtils.copyProperties(salarySheetVO, salarySheetPO);
        if (salarySheetPO.getType() == 2) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, Calendar.DECEMBER, 1, 0, 0, 0);  //年月日  也可以具体到时分秒如calendar.set(2015, 10, 12,11,32,52);
            Date toset = calendar.getTime();        //date就是你需要的时间
            salarySheetPO.setCreateDate(toset);
        } else {
            salarySheetPO.setCreateDate(new Date());
        }
        SalarySheetPO latest = salarySheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "GZD");
        salarySheetPO.setId(id);
        salarySheetPO.setState(state);
        salarySheetDao.saveSheet(salarySheetPO);
    }

    /**
     * 逻辑：总经理生成年终奖才能进行12月份的工资生成
     *
     * @return
     */
    @Override
    public List<SalarySheetVO> generateSalary() {
        List<StaffPO> staffPOS = staffService.queryAllPO();
        List<SalarySheetVO> salarySheetVOS = new ArrayList<>();
        List<Date> dates = TimeUtils.getFirstAndLastDayOfMonth();
        int year = TimeUtils.getYear();

        for (StaffPO staffPO : staffPOS) {
            //HR判断是否已经有年终奖的记录,如果有才能进行制定12月份工资
            if (TimeUtils.getMonth() == Calendar.DECEMBER && salarySheetDao.findIfAnnualHasBeenMade(staffPO.getId(), TimeUtils.getYear()) == 0)
                continue;

            //检查本月是否已经生成了员工工资,本年是否已经生成了总经理的工资
            int ifSalaryHasBeenMade = salarySheetDao.findIfSalaryHasBeenMade(staffPO.getId(), dates.get(0), dates.get(1));//总记录（包括总经理）
            ifSalaryHasBeenMade += salarySheetDao.findIfSalaryHasBeenMadeGM(staffPO.getId(), year);                         //总经理记录
            if (ifSalaryHasBeenMade >= 1) continue;

            SalarySheetVO SSVO = new SalarySheetVO();
            SSVO.setStaffId(staffPO.getId());
            SSVO.setStaffName(staffPO.getName());
            SSVO.setCardAccount(staffPO.getCardAccountId());
            SSVO.setType(1);

            List<BigDecimal> salaries = context.generateSalary(staffPO);
            SSVO.setDueSalary(salaries.get(0));
            SSVO.setTax(salaries.get(1));
            SSVO.setActualSalary(salaries.get(2));

            //如果是12月且有年终奖记录，则需要加到12月的工资记录中，注意，年终奖的记录还是存在的，并没有销毁
            if (TimeUtils.getMonth() == Calendar.DECEMBER) {
                SalarySheetPO annual = salarySheetDao.findAnnual(staffPO.getId(), year);
                SSVO.setDueSalary(SSVO.getDueSalary().add(annual.getDueSalary()));
                SSVO.setTax(SSVO.getTax().add(annual.getTax()));
                SSVO.setActualSalary(SSVO.getActualSalary().add(annual.getActualSalary()));
            }

            salarySheetVOS.add(SSVO);
        }
        return salarySheetVOS;
    }

    /**
     * 捋一遍生成年终奖的逻辑：
     * 总经理点击生成年终奖->按照策略生成年终奖（注意避免税收陷阱）->直接将状态设置为“审批通过”
     * <p>
     * 如果到了12月：
     * 1. 总经理生成了年终奖记录，则HR可以进行12月工资的生成
     * 2. 总经理尚未生成年终奖，则HR不能进行12月工资的生成
     * <p>
     * 前端：年终奖记录单独陈列
     *
     * @return
     */
    @Override
    public List<SalarySheetVO> generateAnnual() {
        List<StaffPO> staffPOS = staffService.queryAllPO();
        List<SalarySheetVO> salarySheetVOS = new ArrayList<>();

        for (StaffPO staffPO : staffPOS) {
            //GM判断是否已经有年终奖的记录,如果有就跳过
            if (salarySheetDao.findIfAnnualHasBeenMade(staffPO.getId(), TimeUtils.getYear()) == 1) continue;

            SalarySheetVO SSVO = new SalarySheetVO();
            SSVO.setStaffId(staffPO.getId());
            SSVO.setStaffName(staffPO.getName());
            SSVO.setCardAccount(staffPO.getCardAccountId());
            SSVO.setType(2);                                                //年终奖

            CalStrategyContext strategy = new CalStrategyContext(staffService, punchInService, saleService);
            List<BigDecimal> salaries = strategy.generateAnnual(staffPO);
            SSVO.setDueSalary(salaries.get(0));
            SSVO.setTax(salaries.get(1));
            SSVO.setActualSalary(salaries.get(2));

            salarySheetVOS.add(SSVO);
        }
        return salarySheetVOS;
    }


    @Override
    @Transactional
    public List<SalarySheetVO> getSalarySheetByState(SalarySheetState state) {
        List<SalarySheetVO> res = new ArrayList<>();
        List<SalarySheetPO> all;
        if (state == null) {
            all = salarySheetDao.findAllSheet();
        } else {
            all = salarySheetDao.findAllSheetByState(state);
        }
        for (SalarySheetPO po : all) {
            SalarySheetVO vo = new SalarySheetVO();
            BeanUtils.copyProperties(po, vo);
            res.add(vo);
        }

        return res;
    }

    @Override
    @Transactional
    public void approval(String salarySheetId, SalarySheetState state) {
        //首先检查要更改的单据状态是否合法
        if (state.equals(SalarySheetState.FAILURE)) {
            SalarySheetPO salarySheet = salarySheetDao.findSheetById(salarySheetId);
            if (salarySheet.getState() == SalarySheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = salarySheetDao.updateSheetState(salarySheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            SalarySheetState prevState;
            if (state.equals(SalarySheetState.SUCCESS)) {
                prevState = SalarySheetState.PENDING_LEVEL_2;
            } else if (state.equals(SalarySheetState.PENDING_LEVEL_2)) {
                prevState = SalarySheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = salarySheetDao.updateSheetStateOnPrev(salarySheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        }
    }

    @Override
    @Transactional
    public SalarySheetVO getSalarySheetById(String salarySheetId) {
        SalarySheetPO salarySheetPO = salarySheetDao.findSheetById(salarySheetId);
        if (salarySheetPO == null) return null;

        SalarySheetVO sVO = new SalarySheetVO();
        BeanUtils.copyProperties(salarySheetPO, sVO);

        return sVO;
    }


    @Override
    @Transactional
    public List<SalarySheetPO> findAllSheetByDate(Date fromDate, Date toDate) {
        return salarySheetDao.findAllSheetByDate(fromDate, toDate);
    }
}
