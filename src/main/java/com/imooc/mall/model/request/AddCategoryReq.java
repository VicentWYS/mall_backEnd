package com.imooc.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 商品分类修改专用类，用于承接商品分类的待修改内容
 * 这里不包括id,createTime和updateTime，目的是防止这些关键信息被黑客修改
 * 单一职责原则：新增商品类信息需用专门的类来承接，
 * 其参数校验注解也为这个类单独设置，实体类中的属性值要保持清洁，以适应多样的情景
 */
public class AddCategoryReq {
    @Size(min = 2, max = 8)
    @NotNull(message = "name不能为null") // 手动设置报错提示信息
    private String name;

    @NotNull(message = "type不能为null")
    @Max(3) // 限制层级数最大为3
    private Integer type;

    @NotNull(message = "parentId不能为null")
    private Integer parentId;

    @NotNull(message = "orderNum不能为null")
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }
}
