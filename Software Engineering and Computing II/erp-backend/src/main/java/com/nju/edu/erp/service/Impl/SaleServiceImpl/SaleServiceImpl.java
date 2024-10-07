package com.nju.edu.erp.service.Impl.SaleServiceImpl;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.Format;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.vo.*;
import com.nju.edu.erp.model.vo.Sale.SaleSheetContentVO;
import com.nju.edu.erp.model.vo.Sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.SaleService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleSheetDao saleSheetDao;

    private final ProductDao productDao;

    private final ProductService productService;

    private final CustomerService customerService;

    private final WarehouseService warehouseService;

    private final PromotionStrategyOnLevelDao promotionStrategyOnLevelDao;

    private final PromotionStrategyOnAmountDao promotionStrategyOnAmountDao;

    @Autowired
    public SaleServiceImpl(SaleSheetDao saleSheetDao, ProductDao productDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService, PromotionStrategyOnLevelDao promotionStrategyOnLevelDao, PromotionStrategyOnAmountDao promotionStrategyOnAmountDao) {
        this.saleSheetDao = saleSheetDao;
        this.productDao = productDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.promotionStrategyOnLevelDao = promotionStrategyOnLevelDao;
        this.promotionStrategyOnAmountDao = promotionStrategyOnAmountDao;
    }

    @Override
    @Transactional
    public void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO) {
        // TODO
        // 需要持久化销售单（SaleSheet）和销售单content（SaleSheetContent），其中总价或者折后价格的计算需要在后端进行
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍

        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO, saleSheetPO);

        saleSheetPO.setOperator(userVO.getName());
        saleSheetPO.setCreateTime(new Date());
        SaleSheetPO latest = saleSheetDao.getLatestSheet();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> sContentPOList = new ArrayList<>();
        for (SaleSheetContentVO content : saleSheetVO.getSaleSheetContent()) {
            SaleSheetContentPO sContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content, sContentPO);
            sContentPO.setSaleSheetId(id);
            BigDecimal unitPrice = sContentPO.getUnitPrice();
            if (unitPrice == null) {
                ProductPO product = productDao.findById(content.getPid());
                unitPrice = product.getPurchasePrice();
                sContentPO.setUnitPrice(unitPrice);
            }

            sContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(sContentPO.getQuantity())));
            sContentPOList.add(sContentPO);
            totalAmount = totalAmount.add(sContentPO.getTotalPrice());
        }

        saleSheetDao.saveBatchSheetContent(sContentPOList);
        saleSheetPO.setRawTotalAmount(totalAmount);
        saleSheetPO.setFinalAmount(totalAmount.multiply(saleSheetPO.getDiscount()).subtract(saleSheetPO.getVoucherAmount()));
        saleSheetDao.saveSheet(saleSheetPO);

    }

    @Override
    @Transactional
    public List<SaleSheetVO> getSaleSheetByState(SaleSheetState state) {
        // TODO
        // 根据单据状态获取销售单（注意：VO包含SaleSheetContent）
        // 依赖的dao层部分方法未提供，需要自己实现
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all;
        if (state == null) {
            all = saleSheetDao.findAllSheet();
        } else {
            all = saleSheetDao.findAllSheetByState(state);
        }
        for (SaleSheetPO po : all) {
            SaleSheetVO vo = new SaleSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleSheetContentPO> alll = saleSheetDao.findContentBySheetId(po.getId());
            List<SaleSheetContentVO> vos = new ArrayList<>();
            for (SaleSheetContentPO p : alll) {
                SaleSheetContentVO v = new SaleSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleSheetContent(vos);
            res.add(vo);
        }

        return res;
    }

    /**
     * 根据销售单id进行审批(state == "待二级审批"/"审批完成"/"审批失败")
     * 在controller层进行权限控制
     *
     * @param saleSheetId 销售单id
     * @param state       销售单要达到的状态
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        // TODO
        // 需要的service和dao层相关方法均已提供，可以不用自己再实现一遍
        /* 一些注意点：
            1. 二级审批成功之后需要进行
                 1. 修改单据状态
                 2. 更新商品表
                 3. 更新客户表
                 4. 新建出库草稿
            2. 一级审批状态不能直接到审批完成状态； 二级审批状态不能回到一级审批状态
         */
        if (state.equals(SaleSheetState.FAILURE)) {
            SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
            if (saleSheet.getState() == SaleSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = saleSheetDao.updateSheetState(saleSheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            SaleSheetState prevState;
            if (state.equals(SaleSheetState.SUCCESS)) {
                prevState = SaleSheetState.PENDING_LEVEL_2;
            } else if (state.equals(SaleSheetState.PENDING_LEVEL_2)) {
                prevState = SaleSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = saleSheetDao.updateSheetStateOnPrev(saleSheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            if (state.equals(SaleSheetState.SUCCESS)) {
                // TODO 审批完成, 修改一系列状态
                // 更新商品表的最新进价
                // 根据purchaseSheetId查到对应的content -> 得到商品id和单价
                // 根据商品id和单价更新商品最近进价recentPp
                List<SaleSheetContentPO> saleSheetContent = saleSheetDao.findContentBySheetId(saleSheetId);
                List<WarehouseOutputFormContentVO> warehouseOutputFormContentVOS = new ArrayList<>();

                for (SaleSheetContentPO content : saleSheetContent) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    productInfoVO.setId(content.getPid());
                    productInfoVO.setRecentRp(content.getUnitPrice());
                    ProductInfoVO res = productService.updateProduct(productInfoVO);

                    WarehouseOutputFormContentVO woContentVO = new WarehouseOutputFormContentVO();
                    woContentVO.setSalePrice(content.getUnitPrice());
                    woContentVO.setQuantity(content.getQuantity());
                    woContentVO.setRemark(content.getRemark());
                    woContentVO.setPid(content.getPid());
                    warehouseOutputFormContentVOS.add(woContentVO);
                }
                // 更新客户表(更新应付字段)
                // 更新应付 receivable
                SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
                CustomerPO customerPO = customerService.findCustomerById(saleSheet.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().add(saleSheet.getFinalAmount()));
                //传入VO作为update的参数
                CustomerVO customerVO = new CustomerVO();
                BeanUtils.copyProperties(customerPO, customerVO);
                customerService.updateCustomer(customerVO);

                // 制定出库单草稿
                // 调用创建出库单的方法
                WarehouseOutputFormVO warehouseOutputFormVO = new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null); // 暂时不填操作人(确认草稿单的时候填写)
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(warehouseOutputFormContentVOS);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);
            }
        }
    }

    /**
     * 获取某个销售人员某段时间内消费总金额最大的客户(不考虑退货情况,销售单不需要审批通过,如果这样的客户有多个，仅保留一个)
     *
     * @param salesman     销售人员的名字
     * @param beginDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return
     */
    public CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman, String beginDateStr, String endDateStr) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date beginTime = dateFormat.parse(beginDateStr);
            Date endTime = dateFormat.parse(endDateStr);
            if (beginTime.compareTo(endTime) > 0) {
                return null;
            } else {
                return saleSheetDao.getMaxAmountCustomerOfSalesmanByTime(salesman, beginTime, endTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据销售单Id搜索销售单信息
     *
     * @param saleSheetId 销售单Id
     * @return 销售单全部信息
     */
    @Override
    public SaleSheetVO getSaleSheetById(String saleSheetId) {
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleSheetId);
        if (saleSheetPO == null) return null;
        List<SaleSheetContentPO> contentPO = saleSheetDao.findContentBySheetId(saleSheetId);
        SaleSheetVO sVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO, sVO);
        List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
        for (SaleSheetContentPO content :
                contentPO) {
            SaleSheetContentVO sContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleSheetContent(saleSheetContentVOList);
        return sVO;
    }

    /**
     * 制定等级的促销策略
     *
     * @param inputVO
     * @return
     */
    @Override
    public void makePromotionStrategyOnLevel(PromotionStrategyOnLevelVO inputVO) {
        PromotionStrategyOnLevelPO savePO = new PromotionStrategyOnLevelPO();
        BeanUtils.copyProperties(inputVO, savePO);
        promotionStrategyOnLevelDao.setPromotionStrategyOnLevel(savePO);
    }

    /**
     * 获得所有的等级促销策略
     *
     * @return
     */
    @Override
    public List<PromotionStrategyOnLevelVO> getAllPromotionStrategyOnLevel() {
        List<PromotionStrategyOnLevelPO> ans = promotionStrategyOnLevelDao.getAllPromotionStrategyOnLevel();
        List<PromotionStrategyOnLevelVO> ansVO = ans.stream().map(promotionStrategyOnLevelPO -> {
            PromotionStrategyOnLevelVO promotionStrategyOnLevelVO = new PromotionStrategyOnLevelVO();
            BeanUtils.copyProperties(promotionStrategyOnLevelPO, promotionStrategyOnLevelVO);
            return promotionStrategyOnLevelVO;
        }).collect(Collectors.toList());
        return ansVO;
    }

    /**
     * 更新等级促销策略
     *
     * @param promotionStrategyOnLevelVO
     */
    @Override
    public PromotionStrategyOnLevelVO updatePromotionStrategyOnLevel(PromotionStrategyOnLevelVO promotionStrategyOnLevelVO) {
        PromotionStrategyOnLevelPO savePO = new PromotionStrategyOnLevelPO();
        BeanUtils.copyProperties(promotionStrategyOnLevelVO, savePO);
        int ans = promotionStrategyOnLevelDao.updateById(savePO);
        //返回更新后的VO
        savePO = promotionStrategyOnLevelDao.findById(savePO.getId());
        PromotionStrategyOnLevelVO res = new PromotionStrategyOnLevelVO();
        BeanUtils.copyProperties(savePO, res);
        return res;
    }

    @Override
    public void deletePromotionStrategyOnLevel(Integer id) {
        promotionStrategyOnLevelDao.deleteById(id);
    }

    @Override
    public void makePromotionStrategyOnAmount(PromotionStrategyOnAmountVO promotionStrategyOnAmountVO) {
        PromotionStrategyOnAmountPO savePO = new PromotionStrategyOnAmountPO();
        BeanUtils.copyProperties(promotionStrategyOnAmountVO, savePO);
        promotionStrategyOnAmountDao.setPromotionStrategyOnAmount(savePO);
    }

    @Override
    public List<PromotionStrategyOnAmountVO> getAllPromotionStrategyOnAmount() {
        List<PromotionStrategyOnAmountPO> ans = promotionStrategyOnAmountDao.getAllPromotionStrategyOnAmount();
        List<PromotionStrategyOnAmountVO> ansVO = ans.stream().map(promotionStrategyOnAmountPO -> {
            PromotionStrategyOnAmountVO promotionStrategyOnAmountVO = new PromotionStrategyOnAmountVO();
            BeanUtils.copyProperties(promotionStrategyOnAmountPO, promotionStrategyOnAmountVO);
            return promotionStrategyOnAmountVO;
        }).collect(Collectors.toList());
        return ansVO;
    }

    @Override
    public PromotionStrategyOnAmountVO updatePromotionStrategyOnAmount(PromotionStrategyOnAmountVO promotionStrategyOnAmountVO) {
        PromotionStrategyOnAmountPO savePO = new PromotionStrategyOnAmountPO();
        BeanUtils.copyProperties(promotionStrategyOnAmountVO, savePO);
        int ans = promotionStrategyOnAmountDao.updateById(savePO);

        savePO = promotionStrategyOnAmountDao.findById(savePO.getId());
        PromotionStrategyOnAmountVO res = new PromotionStrategyOnAmountVO();
        BeanUtils.copyProperties(savePO, res);
        return res;
    }

    @Override
    public void deletePromotionStrategyOnAmount(Integer id) {
        promotionStrategyOnAmountDao.deleteById(id);
    }

    @Override
    public double findVolumeOfTrade(String name, Date t1, Date t2) {
        return saleSheetDao.findVolumeOfTrade(name, t1, t2);
    }

    @Override
    public int findTransXByNameAndPeriod(String name, Date t1, Date t2) {
        return saleSheetDao.findTransXByNameAndPeriod(name, t1, t2);
    }
}
