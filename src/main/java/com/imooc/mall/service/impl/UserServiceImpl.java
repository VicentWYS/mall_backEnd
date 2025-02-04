package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.UserMapper;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(14);
    }

    @Override
    public void register(String userName, String password) throws ImoocMallException {
        // 查询用户名是否存在；不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        // 新用户信息包装为User对象
        User user = new User();
        user.setUsername(userName);
        // user.setPassword(password);
        try {
            user.setPassword(MD5Utils.getMD5Str(password)); // 对密码采用MD5加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // 将User对象写入数据库
        int count = userMapper.insertSelective(user); // 注册一般不全填；返回插入数据成功条数
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }

    }

    @Override
    public User login(String userName, String password) throws ImoocMallException {
        // 获取加密后的密码
        String md5Password = null;

        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // 这种会抛出Exception类，系统异常
        }

        // 查询用户是否已注册过
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) { // 若用户未注册，抛出异常
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }

        return user;
    }

    @Override
    public void updateInformation(User user) throws ImoocMallException {
        // 更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);

        if (updateCount > 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    // 检查用户身份
    @Override
    public boolean checkAdminRole(User user) {
        // 1--普通用户，2--管理员
        return user.getRole().equals(2);
    }
}
