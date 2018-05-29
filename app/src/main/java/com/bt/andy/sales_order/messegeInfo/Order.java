package com.bt.andy.sales_order.messegeInfo;

import java.util.List;

public class Order {
    //会员名
    private String username;
    //会员手机号
    private String usermobile;
    //业务类型
    private String businesstype;
    //收货地址
    private String address;
    //商品详情
    private List<SubtableInfo> subList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
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
}
