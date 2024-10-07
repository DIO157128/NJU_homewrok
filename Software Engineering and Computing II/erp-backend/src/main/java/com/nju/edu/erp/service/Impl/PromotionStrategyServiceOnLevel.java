package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.PromotionStrategyOnLevelDao;
import com.nju.edu.erp.model.po.PromotionStrategyOnLevelPO;
import com.nju.edu.erp.model.vo.PromotionStrategyOnLevelVO;
import com.nju.edu.erp.service.PromotionStrategyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.lang.Integer;

@Service
public class PromotionStrategyServiceOnLevel implements PromotionStrategyService {
    private PromotionStrategyOnLevelDao promotionStrategyOnLevelDao;

    Integer level;


    public PromotionStrategyServiceOnLevel(PromotionStrategyOnLevelDao promotionStrategyOnLevelDao){
        this.promotionStrategyOnLevelDao = promotionStrategyOnLevelDao;
    }

    public void setLevel(Integer level){
        this.level=level;
    }

    @Override
    public BigDecimal getDiscount(){
        List<PromotionStrategyOnLevelPO> promotionStrategyOnLevelPOS=promotionStrategyOnLevelDao.getStrategyOnOneLevel(level);
        BigDecimal discount=new BigDecimal(1);
        Date currentDate=new Date();
        for(PromotionStrategyOnLevelPO promotionStrategyOnLevelPO:promotionStrategyOnLevelPOS){
            if(currentDate.after(promotionStrategyOnLevelPO.getStart_time())&&currentDate.before(promotionStrategyOnLevelPO.getEnd_time())){
                discount=promotionStrategyOnLevelPO.getDiscount();
                break;
            }
        }
        return discount;
    }

    @Override
    public BigDecimal getVoucher(){
        List<PromotionStrategyOnLevelPO> promotionStrategyOnLevelPOS=promotionStrategyOnLevelDao.getStrategyOnOneLevel(level);
        BigDecimal voucher=new BigDecimal(0);
        Date currentDate=new Date();
        for(PromotionStrategyOnLevelPO promotionStrategyOnLevelPO:promotionStrategyOnLevelPOS){
            if(currentDate.after(promotionStrategyOnLevelPO.getStart_time())&&currentDate.before(promotionStrategyOnLevelPO.getEnd_time())){
                voucher=promotionStrategyOnLevelPO.getVoucher();
                break;
            }
        }
        return voucher;
    }

}
