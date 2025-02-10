package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品Service实现类
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    // 添加商品
    @Override
    public void add(AddProductReq addProductReq) throws ImoocMallException {
        // 将请求对象“扩充”为商品对象（使其成为一个完整的商品对象）
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);

        // 校验是否重名
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        // 插入数据
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }
    }

    // 更新商品
    @Override
    public void update(Product updateProduct) throws ImoocMallException {
        // 校验：若需更新商品名，则待更新商品名与现存商品名是否重复
        if (updateProduct.getName() != null) { // 若待更新记录已存在
            Product productOld = productMapper.selectByName(updateProduct.getName());
            if (productOld != null && !productOld.getId().equals(updateProduct.getId())) {
                // 同名 && 不同id （一对多情况）
                throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
        }

        // 更新数据
        int count = productMapper.updateByPrimaryKeySelective(updateProduct);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    // 删除商品
    @Override
    public void delete(Integer id) throws ImoocMallException {
        // 查询待删除记录
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null) { // 不存在要删除的记录，删除失败
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }

        // 删除数据
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    // 批量上下架商品
    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }
}
