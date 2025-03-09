package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.dao.CartMapper;
import com.imooc.mall.model.dao.OrderItemMapper;
import com.imooc.mall.model.dao.OrderMapper;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Order;
import com.imooc.mall.model.pojo.OrderItem;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.model.vo.OrderItemVO;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.CartService;
import com.imooc.mall.service.OrderService;
import com.imooc.mall.util.OrderCodeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    CartService cartService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    /**
     * 生成订单
     *
     * @param createOrderReq 前台传入参数对象（收货人、收货人手机号、收货地址、邮费、支付方式）
     * @return 订单号（String）
     * @throws ImoocMallException 业务异常
     */
    @Transactional(rollbackFor = Exception.class) // 数据库遇到任何异常都会回滚，撤回本次的所有操作
    @Override
    public String create(CreateOrderReq createOrderReq) throws ImoocMallException {
        // 拿到当前用户id
        Integer userId = UserFilter.currentUser.getId();

        // 从购物车中查找已经勾选的商品信息
        List<CartVO> cartVOList = cartService.list(userId); // 查询该用户购物车中（已上架）的商品
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            if (cartVO.getSelected().equals(Constant.Cart.CHECKED)) { // 找出购物车中被选中的商品
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList = cartVOListTemp;

        // 如果购物车已勾选的为空，报错
        if (CollectionUtils.isEmpty(cartVOList)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
        }

        // 拿到用户勾选的商品后，检查商品是否上架、库存足够（以妨加入购物车后，后台改变商品状态）
        vaildSaleStatusAndStock(cartVOList);

        // 转换：购物车中的商品信息 --> 订单对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);

        // 扣库存
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId()); // 商品表中现数量
            int newStock = product.getStock() - orderItem.getQuantity(); // 用户下单后商品更新库存

            if (newStock < 0) { // 若当前库存少于下单数量
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
            }

            // 执行扣库存操作
            product.setStock(newStock); // 为该商品设置新库存
            int count = productMapper.updateByPrimaryKeySelective(product);
            if (count < 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.SYSTEM_ERROR);
            }
        }

        // 删除购物车中对应商品记录
        cleanCart(cartVOList);

        // 开始生成订单
        Order order = new Order();
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId)); // 按照一定规则生成订单号
        order.setOrderNo(orderNo); // 订单号id
        order.setUserId(userId); // 用户id
        order.setTotalPrice(getTotalPrice(orderItemList)); // 所有下单商品的总价
        order.setReceiverName(createOrderReq.getReceiverName()); // 收货人姓名
        order.setReceiverMobile(createOrderReq.getReceiverMobile()); // 收货人电话
        order.setReceiverAddress(createOrderReq.getReceiverAddress()); // 收货地址
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode()); // 订单状态（自定义枚举类实现）
        order.setPostage(0); // 本项目默认邮费都是0（包邮）
        order.setPaymentType(1); // 本项目默认都是在线支付（对应码是1）

        // 将订单对象插入到数据库
        orderMapper.insertSelective(order);

        // 循环保存每个商品到order_item表
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo()); // 设置订单详情记录对应的订单号
            orderItemMapper.insertSelective(orderItem); // 插入到数据库
        }

        // 返回结果
        return orderNo;
    }

    /**
     * 获取订单的总价
     *
     * @param orderItemList 所有相关的订单详情
     * @return 订单总价
     */
    private Integer getTotalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    /**
     * 删除购物车表中对应的商品记录
     *
     * @param cartVOList 购物车商品列表
     */
    private void cleanCart(List<CartVO> cartVOList) throws ImoocMallException {
        for (CartVO cartVO : cartVOList) {
            int count = cartMapper.deleteByPrimaryKey(cartVO.getId()); // 删除购物车表中的相关记录
            if (count < 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.SYSTEM_ERROR);
            }
        }
    }

    /**
     * 转换：购物车中的商品信息 --> 订单对象
     *
     * @param cartVOList 用户购物车中商品信息
     * @return 订单详情列表
     */
    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>(); // 该用户的订单详情列表

        for (int i = 0; i < cartVOList.size(); i++) {
            // 获取购物车中每个商品
            CartVO cartVO = cartVOList.get(i);

            // 创建该商品对应的订单详情中每个商品的记录
            OrderItem orderItem = new OrderItem();

            // 向order_item中填充信息
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setProductName(cartVO.getProductName());

            // 将订单详情对象添加到列表
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    /**
     * 判断用户购物车中所有商品是否满足条件
     * 方法无返回值。若不通过，直接抛出异常
     *
     * @param cartVOList 用户购物车中的所有商品
     * @throws ImoocMallException 业务异常
     */
    private void vaildSaleStatusAndStock(List<CartVO> cartVOList) throws ImoocMallException {
        for (int i = 0; i < cartVOList.size(); i++) {
            // 获取购物车中的每个商品记录
            CartVO cartVO = cartVOList.get(i);

            // 判断商品是否上架
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE); // 该商品不存在或已下架
            }

            // 判断商品数量是否合法
            if (cartVO.getQuantity() <= 0) { // 购物车中商品数量至少为1件
                throw new ImoocMallException(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
            }

            // 判断商品库存是否足够
            if (cartVO.getQuantity() > product.getStock()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
            }
        }
    }

    /**
     * 根据订单号查询订单及订单详情
     *
     * @param orderNo 订单号
     * @return 订单及订单详情对象 orderVO
     * @throws ImoocMallException 业务异常
     */
    @Override
    public OrderVO detail(String orderNo) throws ImoocMallException {
        // 查询某个订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }

        // 校验该订单是否属于当前用户
        Integer userId = UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
        }

        // 某订单对象 --> 订单详情中的所有商品 --> OrderVO对象
        OrderVO orderVO = getOrderVO(order);

        return orderVO;
    }

    /**
     * 某订单对象 --> 订单详情中的所有商品 --> OrderVO对象
     * order --> orderItemList --> orderItemVOList --> orderVO
     *
     * @param order Order对象
     * @return OrderVO对象
     */
    private OrderVO getOrderVO(Order order) throws ImoocMallException {
        OrderVO orderVO = new OrderVO();

        // 复制大部分相同的属性
        BeanUtils.copyProperties(order, orderVO);

        // 设置订单的orderItemVOList（从订单详情表查出并赋值）
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);

        // 设置订单状态名称
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());

        return orderVO;
    }

    /**
     * 获取指定用户的全部订单
     *
     * @param pageNum  分页：当前页码
     * @param pageSize 分页：每页显示的记录数
     * @return 分页对象
     * @throws ImoocMallException 业务异常
     */
    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize) throws ImoocMallException {
        // 获取指定用户id的Order列表
        Integer userId = UserFilter.currentUser.getId();
        PageHelper.startPage(pageNum, pageSize);

        // Order列表 --> OrderVO列表
        List<Order> orderList = orderMapper.selectForCustomer(userId); // 只包含当前页的数据
        if (orderList == null || orderList.isEmpty()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);

        // 包装为PageInfo对象
        PageInfo pageInfo = new PageInfo<>(orderList); // orderVOList 覆盖默认的orderList
        pageInfo.setList(orderVOList);

        return pageInfo;
    }

    /**
     * 订单列表 --> 订单VO列表
     * OrderList --> OrderVOList
     *
     * @param orderList 订单列表
     * @return 订单VO列表
     * @throws ImoocMallException 业务异常
     */
    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) throws ImoocMallException {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = getOrderVO(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

}
