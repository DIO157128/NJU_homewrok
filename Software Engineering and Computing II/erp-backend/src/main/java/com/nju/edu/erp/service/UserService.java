package com.nju.edu.erp.service;

import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {

    /**
     * 用户登录
     * @param userVO
     * @return
     */
    Map<String, String> login(UserVO userVO);

    /**
     * 用户注册
     * @param userVO
     */
    void register(UserVO userVO);

    /**
     * 用户认证
     * @param token
     */
    UserVO auth(String token);

    /**
     * 根据用户名查询
     * @param userName
     * @return
     */
    User findByUsername(String userName);
}
