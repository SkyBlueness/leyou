package com.leyou.order.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.crypto.Data;

@Table(name = "tb_order_status")
public class OrderStatus {

    @Id
    private Long orderId;
    private Integer status;
    private Data createTime;
    private Data paymentTime;
    private Data consignTime;
    private Data endTime;
    private Data closeTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Data createTime) {
        this.createTime = createTime;
    }

    public Data getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Data paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Data getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Data consignTime) {
        this.consignTime = consignTime;
    }

    public Data getEndTime() {
        return endTime;
    }

    public void setEndTime(Data endTime) {
        this.endTime = endTime;
    }

    public Data getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Data closeTime) {
        this.closeTime = closeTime;
    }

    public Data getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Data commentTime) {
        this.commentTime = commentTime;
    }

    private Data commentTime;
}
