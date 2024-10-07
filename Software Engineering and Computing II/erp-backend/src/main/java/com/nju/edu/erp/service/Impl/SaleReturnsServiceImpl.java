package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.sheetState.SaleReturnsSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.po.sale.SaleSheetContentPO;
import com.nju.edu.erp.model.po.sale.SaleSheetPO;
import com.nju.edu.erp.model.po.saleReturns.SaleReturnsSheetContentPO;
import com.nju.edu.erp.model.po.saleReturns.SaleReturnsSheetPO;
import com.nju.edu.erp.model.po.warehouse.WarehouseOutputSheetContentPO;
import com.nju.edu.erp.model.po.warehouse.WarehousePO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.UserVO;

import com.nju.edu.erp.model.vo.saleReturns.SaleReturnsSheetContentVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnsSheetVO;
import com.nju.edu.erp.service.CustomerService;
import com.nju.edu.erp.service.ProductService;
import com.nju.edu.erp.service.SaleReturnsService;
import com.nju.edu.erp.service.WarehouseService;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SaleReturnsServiceImpl implements SaleReturnsService {

    SaleReturnsSheetDao saleReturnsSheetDao;

    ProductService productService;

    ProductDao productDao;

    SaleSheetDao saleSheetDao;

    CustomerService customerService;

    WarehouseService warehouseService;

    WarehouseDao warehouseDao;

    WarehouseOutputSheetDao warehouseOutputSheetDao;

    @Autowired
    public SaleReturnsServiceImpl(SaleReturnsSheetDao saleReturnsSheetDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService, ProductDao productDao, SaleSheetDao saleSheetDao, WarehouseDao warehouseDao,WarehouseOutputSheetDao warehouseOutputSheetDao) {
        this.saleReturnsSheetDao = saleReturnsSheetDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.productDao = productDao;
        this.saleSheetDao = saleSheetDao;
        this.warehouseDao = warehouseDao;
        this.warehouseOutputSheetDao=warehouseOutputSheetDao;
    }

    /**
     * 制定进货退货单
     *
     * @param saleReturnsSheetVO 进货退货单
     */
    @Override
    @Transactional
    public void makeSaleReturnsSheet(UserVO userVO, SaleReturnsSheetVO saleReturnsSheetVO) {
        SaleReturnsSheetPO saleReturnsSheetPO = new SaleReturnsSheetPO();
        BeanUtils.copyProperties(saleReturnsSheetVO, saleReturnsSheetPO);
        // 此处根据制定单据人员确定操作员
        saleReturnsSheetPO.setOperator(userVO.getName());
        saleReturnsSheetPO.setCreateTime(new Date());
        SaleReturnsSheetPO latest = saleReturnsSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSTHD");
        saleReturnsSheetPO.setId(id);
        saleReturnsSheetPO.setState(SaleReturnsSheetState.PENDING_LEVEL_1);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> saleSheetContent = saleSheetDao.findContentBySheetId(saleReturnsSheetPO.getSaleSheetId());
        Map<String, SaleSheetContentPO> map = new HashMap<>();
        for (SaleSheetContentPO item : saleSheetContent) {
            map.put(item.getPid(), item);
        }
        List<SaleReturnsSheetContentPO> pContentPOList = new ArrayList<>();
        for (SaleReturnsSheetContentVO content : saleReturnsSheetVO.getSaleReturnsSheetContent()) {
            SaleReturnsSheetContentPO pContentPO = new SaleReturnsSheetContentPO();
            BeanUtils.copyProperties(content, pContentPO);
            pContentPO.setSaleReturnsSheetId(id);
            SaleSheetContentPO item = map.get(pContentPO.getPid());
            pContentPO.setUnitPrice(item.getUnitPrice());

            BigDecimal unitPrice = pContentPO.getUnitPrice();
            pContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(pContentPO.getQuantity())));
            pContentPOList.add(pContentPO);
            totalAmount = totalAmount.add(pContentPO.getTotalPrice());
        }
        saleReturnsSheetDao.saveBatch(pContentPOList);
        saleReturnsSheetPO.setTotalAmount(totalAmount);
        saleReturnsSheetDao.save(saleReturnsSheetPO);
    }

    /**
     * 根据状态获取进货退货单[不包括content信息](state == null 则获取所有进货退货单)
     *
     * @param state 进货退货单状态
     * @return 进货退货单
     */
    @Override
    @Transactional
    public List<SaleReturnsSheetVO> getSaleReturnsSheetByState(SaleReturnsSheetState state) {
        List<SaleReturnsSheetVO> res = new ArrayList<>();
        List<SaleReturnsSheetPO> all;
        if (state == null) {
            all = saleReturnsSheetDao.findAll();
        } else {
            all = saleReturnsSheetDao.findAllByState(state);
        }
        for (SaleReturnsSheetPO po : all) {
            SaleReturnsSheetVO vo = new SaleReturnsSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleReturnsSheetContentPO> alll = saleReturnsSheetDao.findContentBySaleReturnsSheetId(po.getId());

            List<SaleReturnsSheetContentVO> vos = new ArrayList<>();
            for (SaleReturnsSheetContentPO p : alll) {
                SaleReturnsSheetContentVO v = new SaleReturnsSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleReturnsSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * 根据进货退货单id进行审批(state == "待二级审批"/"审批完成"/"审批失败")
     * 在controller层进行权限控制
     *
     * @param saleReturnsSheetId 销售退货单id
     * @param state              销售退货单要达到的状态
     */
    @Override
    @Transactional
    public void approval(String saleReturnsSheetId, SaleReturnsSheetState state) {
        System.out.println("Mystate:   "+state.getValue());

        SaleReturnsSheetPO saleReturnsSheet = saleReturnsSheetDao.findOneById(saleReturnsSheetId);
        if (state.equals(SaleReturnsSheetState.FAILURE)) {
            if (saleReturnsSheet.getState() == SaleReturnsSheetState.SUCCESS) throw new RuntimeException("状态更新失败");
            int effectLines = saleReturnsSheetDao.updateState(saleReturnsSheetId, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
        } else {
            SaleReturnsSheetState prevState;
            if (state.equals(SaleReturnsSheetState.SUCCESS)) {
                prevState = SaleReturnsSheetState.PENDING_LEVEL_2;
            } else if (state.equals(SaleReturnsSheetState.PENDING_LEVEL_2)) {
                prevState = SaleReturnsSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("状态更新失败");
            }
            int effectLines = saleReturnsSheetDao.updateStateV2(saleReturnsSheetId, prevState, state);
            if (effectLines == 0) throw new RuntimeException("状态更新失败");
            if (state.equals(SaleReturnsSheetState.SUCCESS)) {
                // TODO 销售退货审批完成, 修改一系列状态
                // 销售退货单id， 关联的销售单id 【 销售退货单id->销售单id->出库单id->批次id】
                String wo_id = warehouseOutputSheetDao.findWoidBySaleid(saleReturnsSheet.getSaleSheetId());

                //获取折扣
                //找voucherAmount-> 根据SaleReturnSheet 按SaleSheetId找SaleSheet
                SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleReturnsSheet.getSaleSheetId());
                BigDecimal voucherAmount = saleSheetPO.getVoucherAmount();
                BigDecimal finalAmount = saleSheetPO.getFinalAmount();
                BigDecimal discount = saleSheetPO.getDiscount();
                List<SaleSheetContentPO>saleSheetContentPOS=saleSheetDao.findContentBySheetId(saleReturnsSheet.getSaleSheetId());
                Integer allProductAmount=0;
                for(SaleSheetContentPO spo:saleSheetContentPOS){
                    allProductAmount+=spo.getQuantity();
                }
                BigDecimal voucherAtEachProduct= voucherAmount.divide(BigDecimal.valueOf(allProductAmount), 3);
                //- 销售退货单id-pid， quantity 【批次id+pid -> 定位到库存的一个条目->库存增加quantity】
                //- 【 pid -> 定位到单位进价->Σ单位进价*quantity=要收回的钱->公司要收回的钱要减少  】
                List<SaleReturnsSheetContentPO> contents = saleReturnsSheetDao.findContentBySaleReturnsSheetId(saleReturnsSheetId);
                BigDecimal receivableToDeduct = BigDecimal.ZERO;
                for (SaleReturnsSheetContentPO content : contents) {
                    String pid = content.getPid();
                    Integer returnQuantity = content.getQuantity();
                    List<WarehouseOutputSheetContentPO> relatedPOs = warehouseOutputSheetDao.getAllContentByPIdWoId(wo_id, pid);
                    for (WarehouseOutputSheetContentPO wopo : relatedPOs) {
                        Integer currentQuantity = wopo.getQuantity();
                        Integer realQuantity;     //实际每次应该更新的数值
                        //TODO 需要在此处处理退货库存，具体方法参照currentQuantity判断，处理的是 warehouse数据库

                        realQuantity = (returnQuantity >= currentQuantity) ? currentQuantity : returnQuantity;
                        WarehousePO warehousePO = warehouseDao.findOneByPidAndBatchId(pid, wopo.getBatchId());
                        if (warehousePO == null) throw new RuntimeException("单据发生错误！请联系管理员！");

                        warehousePO.setQuantity(realQuantity);
                        //BUG: 如果仓库中已经不存在该产品了，也就无法找到该行进行更新，此时应该执行插入操作
                        warehouseDao.increaseQuantity(warehousePO);

                        //更新product库存
                        ProductInfoVO productInfoVO = productService.getOneProductByPid(pid);
                        productInfoVO.setQuantity(productInfoVO.getQuantity() + realQuantity);
                        productService.updateProduct(productInfoVO);
//                        System.out.println("更新后的数量是："+productService.getOneProductByPid(pid).getQuantity());
//                        System.out.println("VoucherAmount:  "+voucherAmount);
//                        System.out.println("FinalAmount:  "+finalAmount);


                        //还需要减去优惠券在该单项上的优惠份额
                        BigDecimal item = content.getUnitPrice().multiply(BigDecimal.valueOf(realQuantity));
                        item=item.multiply(discount);
                        BigDecimal voucherAtThisProduct=voucherAtEachProduct.multiply(BigDecimal.valueOf(realQuantity));
                        receivableToDeduct = receivableToDeduct.add(item);
                        receivableToDeduct = receivableToDeduct.subtract(voucherAtThisProduct);
                        //TODO 写入库单
                        //参考purchase的那个，发现它退回的时候也没有写出库单，但是不写入库单就无法进行审批，出仓
//                        System.out.println("item:   "+item);
//                        System.out.println("content.getUnitPrice:   "+content.getUnitPrice());
//                        System.out.println("realQuantity:   "+realQuantity);
                        returnQuantity -= realQuantity;
                        if (returnQuantity.equals(0)) break;

                    }
                }


//                System.out.println("woid:   "+wo_id);
//                System.out.println("receivableToDeduct:  "+receivableToDeduct);

                //TODO 对 customer数据库进行操作
                Integer supplier = saleSheetPO.getSupplier();
                CustomerPO customer = customerService.findCustomerById(supplier);
                //应收款减少
//                System.out.println("beforeReceivable"+customer.getReceivable());
                customer.setReceivable(customer.getReceivable().subtract(receivableToDeduct));
                //传入VO作为方法的参数
                CustomerVO customerVO = new CustomerVO();
                BeanUtils.copyProperties(customer, customerVO);
                customerService.updateCustomer(customerVO);
//                System.out.println("afterReceivable"+customer.getReceivable());
            }
        }
    }
}
