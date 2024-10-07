package com.nju.edu.erp.dao;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.ProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CustomerDao {
    //增加客户
    void addOne(CustomerPO customerPO);

    //删除客户
    int deleteById(Integer id);

    //更新客户
    int updateOne(CustomerPO customerPO);

    //查询全部的客户
    List<CustomerPO> findAll();

    //根据id查询客户
    CustomerPO findOneById(Integer supplier);

    //根据类别查询客户
    List<CustomerPO> findAllByType(CustomerType customerType);

    CustomerPO findOneByName(String name);
}
