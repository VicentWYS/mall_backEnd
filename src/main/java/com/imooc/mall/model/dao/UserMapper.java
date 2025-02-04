package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByName(String userName);

    // 根据用户名和密码寻找记录（这里有一个以上的参数，需加注解）
    User selectLogin(@Param("userName") String userName, @Param("password") String password);
}