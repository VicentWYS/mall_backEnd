package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    // 通过商品名查找商品是否存在
    Product selectByName(String name);

    // 批量上下架
    int batchUpdateSellStatus(@Param("ids") Integer[] ids, @Param("sellStatus") Integer sellStatus);

    // 后台获取所有商品
    List<Product> selectListForAdmin();

    // 前台（根据规则）获取所有商品
    List<Product> selectList(@Param("query") ProductListQuery productListQuery);

}