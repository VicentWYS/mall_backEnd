package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Cart;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车Service实现类
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    /**
     * 获取购物车商品列表
     *
     * @param userId 当前用户id
     * @return 该购物车中的商品列表
     */
    @Override
    public List<CartVO> list(Integer userId) {
        // 获取购物车商品列表
        List<CartVO> cartVOS = cartMapper.selectList(userId);

        // 计算每条记录中的商品总价
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i); // 这里是引用类型，指向的是cartVOS中的这个元素对象
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOS;
    }

    /**
     * 购物车添加商品
     * 返回购物车中的商品列表
     *
     * @param userId    用户id
     * @param productId （待添加）商品id
     * @param count     （待添加）商品数量
     * @return 该购物车中的商品列表
     * @throws ImoocMallException 业务异常
     */
    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) throws ImoocMallException {
        // 检验指定商品是否满足添加购物车条件
        validProduct(productId, count);

        // 该商品是否之前就在购物车中
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) { // 购物车中没有该商品
            // 为该用户的购物车新增一条（商品）记录
            cart = new Cart();
            cart.setProductId(productId); // 商品id
            cart.setUserId(userId); // 用户id
            cart.setQuantity(count); // 商品数量
            cart.setSelected(Constant.Cart.CHECKED); // 默认为选中
            cartMapper.insertSelective(cart);
        } else {
            // 商品已在购物车中，只需叠加数量
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setId(cart.getId());
            cartNew.setProductId(productId); // 商品id
            cartNew.setUserId(userId); // 用户id
            cartNew.setQuantity(count); // 商品数量
            cartNew.setSelected(Constant.Cart.CHECKED); // 默认为选中（既然已经要添加商品了，自然认为用户选中该商品）
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }

        return this.list(userId);
    }

    // 检验指定商品是否满足添加购物车条件
    private void validProduct(Integer productId, Integer count) throws ImoocMallException {
        // 判断商品是否在售，是否有足够库存
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE); // 该商品不存在或已下架
        }

        // 判断商品数量是否合法
        if (count <= 0) { // 购物车中商品数量至少为1件
            throw new ImoocMallException(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
        }

        // 判断商品库存
        if (count > product.getStock()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
        }
    }

    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) throws ImoocMallException {
        // 检验指定商品是否满足添加购物车条件
        validProduct(productId, count);

        // 该商品是否之前就在购物车中
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) { // 若购物车中没有该商品，抛出异常
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        } else {
            // 若商品已在购物车中，则更新数量
            Cart cartNew = new Cart();
            cartNew.setId(cart.getId());
            cartNew.setProductId(productId); // 商品id
            cartNew.setUserId(userId); // 用户id
            cartNew.setQuantity(count); // 商品数量（该方法的核心）
            cartNew.setSelected(Constant.Cart.CHECKED); // 默认为选中（既然已经要添加商品了，自然认为用户选中该商品）
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }

        return this.list(userId);
    }

    /**
     * 删除购物车指定商品记录
     *
     * @param userId    用户id
     * @param productId 商品id
     * @return 购物车现存商品列表
     * @throws ImoocMallException 业务异常
     */
    @Override
    public List<CartVO> delete(Integer userId, Integer productId) throws ImoocMallException {
        // 该商品是否之前就在购物车中
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) { // 若购物车中没有该商品，抛出异常
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        } else {
            // 若商品已在购物车中，则可以删除这条商品记录
            cartMapper.deleteByPrimaryKey(cart.getId()); // 根据Cart的id属性定位
        }

        return this.list(userId);
    }
}
