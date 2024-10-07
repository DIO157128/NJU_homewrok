package com.nju.edu.erp.dao;


import com.nju.edu.erp.model.po.PostPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class PostDaoTest {
    @Autowired
    PostDao postDao;


    @Test
    @Transactional
    @Rollback(value = true)
    void queryAllPostTest() {
        List<PostPO> postPOS = postDao.queryAllPost();
        assertNotNull(postPOS);
        assert (postPOS.size()==8);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findByPostIdTest() {
        PostPO po = postDao.findByPostId(3);
        assertNotNull(po);
        assertEquals(po.getBasicSalary(),5000);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findIfHasPostTest() {
        int ifHasPost = postDao.findIfHasPost(3);
        assertEquals(1,ifHasPost);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void findIfHasPost_noTest() {
        int ifHasPost = postDao.findIfHasPost(200);
        assertEquals(0,ifHasPost);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void deletePostTest() {

        postDao.deletePost(3);
        int ifHasPost = postDao.findIfHasPost(3);
        assertEquals(ifHasPost,0);
    }


    @Test
    @Transactional
    @Rollback(value = true)
    void updatePost_not_foundTest() {
        PostPO postPO=PostPO.builder().postName("GM").id(200).basicSalary(11000).postSalary(11111).calSalaryMethod("PERCENTAGE").paySalaryMethod("CARD").taxDeductionRemark("无").build();
        int num = postDao.updatePost(postPO);
        assertEquals(0,num);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    void updatePostTest() {
        PostPO postPO=PostPO.builder().postName("GM").id(2).basicSalary(11000).postSalary(11111).calSalaryMethod("PERCENTAGE").paySalaryMethod("CARD").taxDeductionRemark("无").build();
        int num = postDao.updatePost(postPO);
        assertEquals(1,num);
    }


    @Test
    @Transactional
    @Rollback(value = true)
    void addPostTest() {
        PostPO postPO=PostPO.builder().postName("GM").basicSalary(11000).postSalary(11111).calSalaryMethod("PERCENTAGE").paySalaryMethod("CARD").taxDeductionRemark("无").build();
        postDao.addPost(postPO);
        List<PostPO> postPOS = postDao.queryAllPost();
        assertNotNull(postPOS);
        assertEquals(9,postPOS.size());
    }


}
