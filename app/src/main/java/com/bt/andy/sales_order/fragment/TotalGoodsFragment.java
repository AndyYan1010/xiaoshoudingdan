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
import com.bt.andy.sales_order.utils.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

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
    private EditText mEdit_phone, mEdit_name;
    private ImageView mImg_delete, mImg_confirm;
    private TextView  mTv_entry;
    private ImageView mImg_scan;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 1001;//申请照相机权限结果
    private int REQUEST_CODE                       = 1002;//接收扫描结果
    private int DETAIL_REQUESTCODE                 = 111;//商品详情页返回参数对应码
    private int ORDER_RESULT_CODE                  = 9527;//商品详情页返回结果码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_total, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mImg_scan = mRootView.findViewById(R.id.img_back);
        mTv_title = mRootView.findViewById(R.id.tv_title);
        mEdit_phone = mRootView.findViewById(R.id.edit_phone);
        mEdit_name = mRootView.findViewById(R.id.edit_name);
        mImg_delete = mRootView.findViewById(R.id.img_delete);
        mImg_confirm = mRootView.findViewById(R.id.img_confirm);
        mTv_entry = mRootView.findViewById(R.id.tv_entry);
    }

    private void initData() {

        mImg_scan.setVisibility(View.VISIBLE);
        mImg_scan.setImageResource(R.drawable.scanning);
        mImg_scan.setOnClickListener(this);
        mTv_title.setText("销售下单");
        mImg_delete.setOnClickListener(this);
        mImg_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
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
                ToastUtils.showToast(getContext(), "等待网络确认");
                mTv_entry.setVisibility(View.VISIBLE);
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
                    ToastUtils.showToast(getContext(), "解析结果:  " + result);
                    //访问网络，获取商品信息，跳转fragment展示，在新的页面确定后添加到listview中
                    getGoodsInfo();
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
                }
            }
        }
    }

    private void getGoodsInfo() {
        //获取商品详情
        //跳转fragment，选择添加
        showGoodsDetail();
    }

    private void showGoodsDetail() {
        //跳转商品详情
        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        startActivityForResult(intent, DETAIL_REQUESTCODE);
    }
}
