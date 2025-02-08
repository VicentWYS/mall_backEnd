package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    Category selectByName(String name);

    // 获取所有商品分类，保存到列表返回
    List<Category> selectList();

    // 查询指定父目录id的所有类别
    List<Category> selectCategoriesByParentId(Integer parentId);
}