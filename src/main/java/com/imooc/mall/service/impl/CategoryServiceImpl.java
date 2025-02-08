package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq) throws ImoocMallException {
        // 将请求对象“扩充”为商品类对象（使其成为一个完整的商品类对象）
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq, category); // 将参数中的对象中属性值复制到新建的这个对象中

        // 校验是否重名
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if (categoryOld != null) { // 若已存在重名商品类
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        // 插入数据
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }

    }

    @Override
    public void update(Category updateCategory) throws ImoocMallException {
        // 校验：若需更新类名，则待更新类名与现存类名是否重复
        if (updateCategory.getName() != null) {
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            if (categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())) {
                // 待更新类名在表中存在 && 两者id不是同一条
                throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
        }

        // 更新数据
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) throws ImoocMallException {
        // 查询待删除记录
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        if (categoryOld == null) { // 不存在要删除的记录，删除失败
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }

        // 删除数据
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        // 设置分页规则，这里排序的关键字与数据库表中一致
        PageHelper.startPage(pageNum, pageSize, "type, order_num");

        // 获取所有商品分类
        List<Category> categoryList = categoryMapper.selectList();

        // 将分类列表包装为一个分页对象
        PageInfo pageInfo = new PageInfo<>(categoryList);

        return pageInfo;
    }

}
