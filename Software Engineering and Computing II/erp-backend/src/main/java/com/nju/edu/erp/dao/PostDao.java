package com.nju.edu.erp.dao;


import com.nju.edu.erp.model.po.PostPO;
import com.nju.edu.erp.model.po.StaffPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PostDao {


    /**
     * 查询所有岗位信息
     * @return
     */
    List<PostPO> queryAllPost();


    /**
     * 根据岗位Id寻找岗位
     * @param postId
     * @return
     */
    PostPO findByPostId(int postId);

    /**
     * 查找对应的 post
     * @param postId
     * @return
     */
    int findIfHasPost(int postId);

    /**
     * 删除岗位
     * @param postId
     * @return
     */
    void deletePost(int postId);

    /**
     * 更新岗位信息
     * @param postPO
     * @return
     */
    int updatePost(PostPO postPO);

    /**
     * 添加岗位
     * @param postPO
     */
    void addPost(PostPO postPO);

    /**
     * 测试使用
     * @return
     */
    int getMaxId();


}
