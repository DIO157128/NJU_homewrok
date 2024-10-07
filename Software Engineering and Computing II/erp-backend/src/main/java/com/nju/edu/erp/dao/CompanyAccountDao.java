package com.nju.edu.erp.dao;

import com.nju.edu.erp.model.po.CompanyAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CompanyAccountDao {

    int addCompanyAccount(CompanyAccountPO companyAccountPO);

    int updateCompanyAccount(CompanyAccountPO companyAccountPO);

    int deleteCompanyAccount(Integer id);

    List<CompanyAccountPO> queryAllAccounts();

    CompanyAccountPO findOneByName(String name);//根据账户名称获取余额

    CompanyAccountPO findOneById(int Id);//根据账户Id获取余额
}