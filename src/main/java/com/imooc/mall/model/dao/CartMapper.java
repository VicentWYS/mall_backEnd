package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    // 【购物车】和【商品】两张表联合查询（购物车商品总价没有在内）
    List<CartVO> selectList(@Param("userId") Integer userId);

    // 查询指定【用户】的购物车中是否存在某个【商品】
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    // 将购物车中某商品设置为：选中/不选中
    Integer selectOrNot(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("selected") Integer selected);
}