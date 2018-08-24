package com.bt.andy.sales_order.messegeInfo;

import java.util.List;

public class Order {
    //用户名
    private String             userId;
    //会员名
    private String             membername;
    //会员手机号
    private String             membermobile;
    //积分
    private String             point;
    //业务类型
    private String             businesstype;
    //摘要
    private String             remark;
    //收货地址
    private String             address;
    //商品详情
    private List<SubtableInfo> subList;
    /**
     * acPhone : 12522
     * dire : 南方
     */
    //收货人手机号
    private String             acPhone;
    //物流方向
    private String             dire;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMembermobile() {
        return membermobile;
    }

    public void setMembermobile(String membermobile) {
        this.membermobile = membermobile;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<SubtableInfo> getSubList() {
        return subList;
    }

    public void setSubList(List<SubtableInfo> subList) {
        this.subList = subList;
    }

    public String getAcPhone() {
        return acPhone;
    }

    public void setAcPhone(String acPhone) {
        this.acPhone = acPhone;
    }

    public String getDire() {
        return dire;
    }

    public void setDire(String dire) {
        this.dire = dire;
    }
}
