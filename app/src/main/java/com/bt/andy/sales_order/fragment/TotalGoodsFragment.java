package com.bt.andy.sales_order.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
    private TextView mTv_entry, mTv_surema, mTv_no_good;
    private ImageView mImg_scan;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 1001;//申请照相机权限结果
    private int REQUEST_CODE                       = 1002;//接收扫描结果
    private int DETAIL_REQUESTCODE                 = 111;//商品详情页返回参数对应码
    private int ORDER_RESULT_CODE                  = 9527;//商品详情页返回结果码
    private MyListView         mLv_goods;
    private List<SubtableInfo> mData;//存放每个子表的数据
    private LvGoodsAdapter     mGoodsAdapter;
    private Button             mBt_submit;

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
        mTv_no_good = mRootView.findViewById(R.id.tv_no_good);//提示未添加商品
        mLv_goods = mRootView.findViewById(R.id.lv_goods);//商品列表
        mBt_submit = mRootView.findViewById(R.id.bt_submit);//总表提交服务器
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
        mLv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        mLv_goods.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //弹出对话框提示用户将要删除该条item
                showDeleteDailog(i);
                return false;
            }
        });
        mBt_submit.setOnClickListener(this);
        mBt_submit.setVisibility(View.GONE);
    }

    private void showDeleteDailog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("温馨提示");
        builder.setMessage("您确定要删除该笔子订单？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mData.remove(position);
                mGoodsAdapter.notifyDataSetChanged();
                if (mData.size() == 0) {
                    mTv_no_good.setVisibility(View.VISIBLE);
                    if (mBt_submit.isShown()) {
                        mBt_submit.setVisibility(View.GONE);
                    }
                }
                dialog.cancel();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
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
            case R.id.bt_submit:
                //TODO:提交总表到服务器

                break;
            default:
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
                    String goodsName = data.getStringExtra("goodsName");
                    String unitPrice = data.getStringExtra("unitPrice");
                    double unit_price = Double.parseDouble(unitPrice);
                    int number = Integer.parseInt(data.getStringExtra("number"));
                    double sumPrice = Double.parseDouble(data.getStringExtra("sumPrice"));
                    String goodsLocalId = data.getStringExtra("goodsLocalId");
                    //填入总表
                    SubtableInfo goodsInfo = new SubtableInfo();
                    goodsInfo.setGoodsName(goodsName);
                    goodsInfo.setZh_unit_price(unit_price);
                    goodsInfo.setNumber(number);
                    goodsInfo.setSum_pric(sumPrice);
                    mData.add(goodsInfo);
                    mGoodsAdapter.notifyDataSetChanged();
                    mTv_no_good.setVisibility(View.GONE);
                    if (!mBt_submit.isShown()) {
                        mBt_submit.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void sendGoodsInfo(String goodsID) {
        ToastUtils.showToast(getContext(), "商品编码：" + goodsID);
        mEdit_goods_id.setText(goodsID);
        //跳转activity，选择添加
        showGoodsDetail(goodsID);
    }

    private void showGoodsDetail(String goodsID) {
        //跳转商品详情
        Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
        intent.putExtra("goodsId",goodsID);
        startActivityForResult(intent, DETAIL_REQUESTCODE);
    }
}
