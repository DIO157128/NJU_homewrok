package com.nju.edu.erp.service;

import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.vo.CustomerVO;

import java.util.List;

public interface CustomerService {
    /**
     * 增加新的客户
     *
     * @para customerPO 客户信息
     */
    void addCustomer(CustomerVO customerVO);

    /**
     * 删除客户
     *
     * @para customerPO 客户信息
     */
    void deleteCustomer(Integer id);

    /**
     * 根据id更新客户信息
     *
     * @param customerVO 客户信息
     */
    void updateCustomer(CustomerVO customerVO);

    /**
     * 返回所有客户
     */
    List<CustomerPO> queryAllCustomers();

    /**
     * 根据type查找对应类型的客户
     *
     * @param type 客户类型
     * @return 客户列表
     */
    List<CustomerPO> getCustomersByType(CustomerType type);

    /**
     * @param id
     * @return
     */
    CustomerPO findCustomerById(Integer id);


    /**
     * @param name
     * @return
     */
    CustomerPO findCustomerByName(String name);

}
