package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.ProductListReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品Service实现类
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

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

    // 后台获取商品列表
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        // 设置分页规则，这里排序的关键字与数据库表中一致
        PageHelper.startPage(pageNum, pageSize);

        // 获取所有商品分类
        List<Product> productList = productMapper.selectListForAdmin();

        // 将分类列表包装为一个分页对象
        PageInfo pageInfo = new PageInfo(productList);

        return pageInfo;
    }

    // 获取指定id的商品的详情
    @Override
    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);

        return product;
    }

    /**
     * 前台获取商品列表
     *
     * @param productListReq 用户选择的查询条件和其他信息
     * @return PageInfo分页对象
     */
    @Override
    public PageInfo list(ProductListReq productListReq) {
        // 构建查询的Query对象
        /*
        查询条件和信息都在参数productListReq中，但是它不能直接用于SQL查询，
        因为用户选择的分类id有可能包含子id，这个需要专门查询出来，将所有相关的分类id形成一个列表，
        因此，专门设置一个类：ProductListQuery来保存于SQL查询中关键词和相关分类id的信息
         */
        ProductListQuery productListQuery = new ProductListQuery();

        // 查询：商品名关键词
        if (!StringUtils.isEmpty(productListReq.getKeyword())) { // 关键词非空
            // 模糊查询：%关键词%
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(keyword); // 设置查询关键字
        }

        // 查询：商品分类及其所有子孙分类id
        if (productListReq.getCategoryId() != null) { // 查询分类id非空
            // 这里获取的是商品类树状结构对象，需提取出分类id的树状结构对象
            List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(productListReq.getCategoryId());

            // 用一个列表保存相关分类id
            ArrayList<Integer> categoryIds = new ArrayList<>();

            // 首先保存查询参数中的分类id
            categoryIds.add(productListReq.getCategoryId()); // 保存当前查询参数中的分类id

            // 递归操作，获取以查询参数中的分类id为最高节点，其下的分类树状结构对象中的所有分类id
            getCategoryIds(categoryVOS, categoryIds);

            // 将分类ids保存到查询类
            productListQuery.setCategoryIds(categoryIds);
        }

        // 排序处理
        String orderBy = productListReq.getOrderBy(); // 查询参数中的排序规则
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) { // 过滤校验：只有符合枚举条件的才考虑排序，防止被无关信息干扰
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else { // 无排序需要
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }

        // 查询操作
        List<Product> products = productMapper.selectList(productListQuery); // SQL中加入查询条件（SQL中设置默认只查询上架商品）
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    /**
     * 递归查询：从商品分类的结构树中，提取出分类id的结构树
     *
     * @param categoryVOS 商品分类结构树
     * @param categoryIds 商品分类id结构树
     */
    private void getCategoryIds(List<CategoryVO> categoryVOS, ArrayList<Integer> categoryIds) {
        for (int i = 0; i < categoryVOS.size(); i++) { // 遍历当前层的目录节点
            CategoryVO categoryVO = categoryVOS.get(i); // 获取当前节点的目录对象
            if (categoryVO != null) { // 节点非空
                categoryIds.add(categoryVO.getId()); // 获取节点对应的分类id
                getCategoryIds(categoryVO.getChildCategory(), categoryIds); // 递归查询
            }
        }
    }
}
