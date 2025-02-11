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
            cart.setId(cart.getId());
            cart.setProductId(productId); // 商品id
            cart.setUserId(userId); // 用户id
            cart.setQuantity(count); // 商品数量
            cart.setSelected(Constant.Cart.CHECKED); // 默认为选中（既然已经要添加商品了，自然认为用户选中该商品）
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }

        return null;
    }

    // 检验指定商品是否满足添加购物车条件
    private void validProduct(Integer productId, Integer count) throws ImoocMallException {
        // 判断商品是否在售，是否有足够库存
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE); // 该商品不存在或已下架
        }

        // 判断商品库存
        if (count > product.getStock()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
        }
    }
}
