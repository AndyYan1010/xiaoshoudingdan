package com.bt.andy.sales_order.messegeInfo;

import java.util.List;

public class Order {
    //会员名
    private String             membername;
    //会员手机号
    private String             membermobile;
    //业务类型
    private String             businesstype;
    //收货地址
    private String             address;
    //商品详情
    private List<SubtableInfo> subList;

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
