package com.bt.andy.sales_order.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.andy.sales_order.R;
import com.bt.andy.sales_order.activity.GoodsDetailActivity;
import com.bt.andy.sales_order.adapter.LvGoodsAdapter;
import com.bt.andy.sales_order.messegeInfo.SubtableInfo;
import com.bt.andy.sales_order.utils.ToastUtils;
import com.bt.andy.sales_order.viewmodle.MyListView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/23 16:39
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class TotalGoodsFragment extends Fragment implements View.OnClickListener {
    private View     mRootView;
    private TextView mTv_title;
    private EditText mEdit_phone, mEdit_name, mEdit_goods_id;
    private ImageView mImg_delete, mImg_confirm;
    private TextView mTv_entry, mTv_surema;
    private ImageView mImg_scan;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 1001;//申请照相机权限结果
    private int REQUEST_CODE                       = 1002;//接收扫描结果
    private int DETAIL_REQUESTCODE                 = 111;//商品详情页返回参数对应码
    private int ORDER_RESULT_CODE                  = 9527;//商品详情页返回结果码
    private MyListView         mLv_goods;
    private List<SubtableInfo> mData;//存放每个子表的数据
    private LvGoodsAdapter     mGoodsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_total, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mTv_title = mRootView.findViewById(R.id.tv_title);
        mEdit_phone = mRootView.findViewById(R.id.edit_phone);//输入手机号
        mEdit_name = mRootView.findViewById(R.id.edit_name);//输入会员名
        mImg_delete = mRootView.findViewById(R.id.img_delete);//清空手机号
        mImg_confirm = mRootView.findViewById(R.id.img_confirm);//查询手机号
        mTv_entry = mRootView.findViewById(R.id.tv_entry);//确认录入会员名
        mImg_scan = mRootView.findViewById(R.id.img_scan);//扫描
        mEdit_goods_id = mRootView.findViewById(R.id.edit_goods_id);//输入商品id
        mTv_surema = mRootView.findViewById(R.id.tv_surema);//确认输入的商品id
        mLv_goods = mRootView.findViewById(R.id.lv_goods);
    }

    private void initData() {
        mTv_title.setText("销售下单");
        mImg_scan.setOnClickListener(this);
        mImg_delete.setOnClickListener(this);
        mImg_confirm.setOnClickListener(this);
        mTv_surema.setOnClickListener(this);
        mData = new ArrayList();
        mGoodsAdapter = new LvGoodsAdapter(getContext(), mData);
        mLv_goods.setAdapter(mGoodsAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_scan:
                //动态申请照相机权限
                //第二个参数是需要申请的权限
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                } else {
                    Intent intent = new Intent(getContext(), CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
            case R.id.img_delete:
                mEdit_phone.setText("");
                break;
            case R.id.img_confirm:
                ToastUtils.showToast(getContext(), "等待网络确认会员号");
                mTv_entry.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_surema:
                String goodsid = String.valueOf(mEdit_goods_id.getText()).trim();
                if (null == goodsid || "".equals(goodsid) || "商品编码".equals(goodsid)) {
                    ToastUtils.showToast(getContext(), "请输入商品编码");
                    return;
                }
                //跳转商品详情界面，携带商品id
                sendGoodsInfo(goodsid);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    //获取商品id信息，跳转activity展示，在新的页面确定后添加到listview中
                    sendGoodsInfo(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (requestCode == DETAIL_REQUESTCODE) {
            if (resultCode == ORDER_RESULT_CODE) {
                String orderInfo = data.getStringExtra("orderDetail");
                if (null != orderInfo) {
                    //填入总表
                    SubtableInfo goodsInfo = new SubtableInfo();
                    goodsInfo.setGoodsName("熊猫Tv9527");
                    goodsInfo.setZh_unit_price(1900);
                    goodsInfo.setNumber(10);
                    goodsInfo.setSum_pric(19000);
                    mData.add(goodsInfo);
                    mGoodsAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void sendGoodsInfo(String goodsID) {
        ToastUtils.showToast(getContext(), "商品编码：" + goodsID);
        mEdit_goods_id.setText(goodsID);
        //跳转activity，选择添加
        showGoodsDetail();
    }

    private void showGoodsDetail() {
        //跳转商品详情
        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        startActivityForResult(intent, DETAIL_REQUESTCODE);
    }
}
