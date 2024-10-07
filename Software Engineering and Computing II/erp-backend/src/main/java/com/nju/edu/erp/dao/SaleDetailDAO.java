package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.SaleDetailPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SaleDetailDAO {
    /**
     * 获得销售明细
     *
     * @param
     * @return
     */
    public List<SaleDetailPO> getSailDetail();
}
