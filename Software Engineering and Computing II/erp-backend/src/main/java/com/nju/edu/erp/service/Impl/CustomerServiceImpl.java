package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.enums.CustomerType;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.CategoryPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.ProductPO;
import com.nju.edu.erp.model.vo.CustomerVO;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * 增加新的客户
     * @param customerVO
     */
    @Override
    public void addCustomer(CustomerVO customerVO) {
        CustomerPO savePO = new CustomerPO();
        BeanUtils.copyProperties(customerVO, savePO);
        savePO.setPayable(BigDecimal.valueOf(0));
        savePO.setReceivable(BigDecimal.valueOf(0));
        savePO.setLineOfCredit(BigDecimal.valueOf(0));
        customerDao.addOne(savePO);
    }

    /**
     * 删除客户
     * @param id
     */
    @Override
    public void deleteCustomer(Integer id) {
        customerDao.deleteById(id);
    }

    /**
     * 更新客户信息
     *
     * @param customerVO 客户信息
     */
    @Override
    public void updateCustomer(CustomerVO customerVO) {
        CustomerPO customerPO = new CustomerPO();
        BeanUtils.copyProperties(customerVO, customerPO);
        customerDao.updateOne(customerPO);
    }

    /**
     * 查询所有的客户
     * @return
     */
    @Override
    public List<CustomerPO> queryAllCustomers() {
        CustomerType supplier = CustomerType.SUPPLIER;
        CustomerType seller = CustomerType.SELLER;
        List<CustomerPO> suppliers = getCustomersByType(supplier);
        List<CustomerPO> sellers = getCustomersByType(seller);
        suppliers.addAll(sellers);
        return suppliers;
    }

    /**
     * 根据type查找对应类型的客户
     *
     * @param type 客户类型
     * @return 客户列表
     */
    @Override
    public List<CustomerPO> getCustomersByType(CustomerType type) {
        return customerDao.findAllByType(type);
    }

    /**
     * 根据id查询客户
     * @param supplier
     * @return
     */
    @Override
    public CustomerPO findCustomerById(Integer supplier) {
        return customerDao.findOneById(supplier);
    }

    @Override
    public CustomerPO findCustomerByName(String name) {
        return customerDao.findOneByName(name);
    }
}
