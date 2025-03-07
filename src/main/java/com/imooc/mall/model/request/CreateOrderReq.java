package com.imooc.mall.model.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 创建订单时，用户从前端需要输入的参数
 */
public class CreateOrderReq {
    // private Integer id;

    // private String orderNo;

    // private Integer userId;

    // private Integer totalPrice;

    @NotNull
    private String receiverName;

    @NotNull
    private String receiverMobile;

    @NotNull
    private String receiverAddress;

    // private Integer orderStatus;

    private Integer postage = 0; // 默认为免邮费（包邮模式）

    private Integer paymentType = 1; // 默认为在线支付（自定义支付方式1为在线支付）

    // private Date deliveryTime;

    // private Date payTime;

    // private Date endTime;

    // private Date createTime;

    // private Date updateTime;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
}