package com.bt.andy.sales_order.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bt.andy.sales_order.BaseActivity;
import com.bt.andy.sales_order.R;
import com.bt.andy.sales_order.adapter.MySpinnerAdapter;
import com.bt.andy.sales_order.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/24 8:45
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImg_back;
    private TextView  mTv_title;
    private int resultCode = 9527;//设置商品详情返回参数对应码
    private TextView mTv_name1;//商品名
    private TextView mTv_name2;//商品名
    private TextView mTv_unit_price;//单价
    private TextView mTv_stock;//库存
    private TextView mTv_reduce;//减法
    private EditText mEdit_num;//购买数量
    private TextView mTv_add;//加法
    private TextView mTv_discount;//折后价
    private TextView mTv_sumprice;//子单总金额
    private Button   mBt_sure;//确认
    private Spinner  mSpinner;//配送类型
    private EditText mEdit_remarks;//备注
    private EditText mEdit_address;//配送地址
    private Button   mBt_submit;//确定下单
    private double goods_price = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setView();
        setData();
    }

    private void setView() {
        mImg_back = findViewById(R.id.img_back);
        mTv_title = findViewById(R.id.tv_title);
        mTv_name1 = findViewById(R.id.tv_name1);
        mTv_unit_price = findViewById(R.id.tv_unit_price);
        mTv_stock = findViewById(R.id.tv_stock);
        mTv_name2 = findViewById(R.id.tv_name2);
        mTv_reduce = findViewById(R.id.tv_reduce);
        mEdit_num = findViewById(R.id.edit_num);
        mTv_add = findViewById(R.id.tv_add);
        mTv_discount = findViewById(R.id.tv_discount);
        mTv_sumprice = findViewById(R.id.tv_sumprice);
        mBt_sure = findViewById(R.id.bt_sure);
        mSpinner = findViewById(R.id.spinner);
        mEdit_remarks = findViewById(R.id.edit_remarks);
        mEdit_address = findViewById(R.id.edit_address);
        mBt_submit = findViewById(R.id.bt_submit);
    }

    private void setData() {
        mImg_back.setVisibility(View.VISIBLE);
        mImg_back.setImageResource(R.drawable.back);
        mImg_back.setOnClickListener(this);
        mTv_title.setText("商品详情");
        mTv_discount.setText("¥" + goods_price);
        mTv_sumprice.setText("¥" + goods_price);
        mTv_reduce.setOnClickListener(this);
        mTv_add.setOnClickListener(this);
        mBt_sure.setOnClickListener(this);
        mEdit_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String snum = String.valueOf(mEdit_num.getText()).trim();
                int number;
                if (null == snum || "".equals(snum)) {
                    number = 1;
                } else {
                    number = Integer.valueOf(snum);
                }
                mTv_sumprice.setText("¥" + number * goods_price);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final List<String> mData = new ArrayList();
        mData.add("选择配送方式");
        mData.add("配送安装");
        mData.add("配送不安装");
        mData.add("自提安装");
        mData.add("自提不安装");
        MySpinnerAdapter adapter = new MySpinnerAdapter(GoodsDetailActivity.this, mData);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = mData.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mBt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                giveUpThisOrder();
                break;
            case R.id.tv_reduce:
                String snum = String.valueOf(mEdit_num.getText()).trim();
                int number;
                if (null == snum || "".equals(snum)) {
                    number = 1;
                } else {
                    number = Integer.valueOf(snum);
                }
                if (number > 1) {
                    number = number - 1;
                }
                String s1 = "" + number;
                mEdit_num.setText(s1);
                mEdit_num.setSelection(s1.length());
                break;
            case R.id.tv_add:
                String snumAdd = String.valueOf(mEdit_num.getText()).trim();
                int numberAdd;
                if (null == snumAdd || "".equals(snumAdd)) {
                    numberAdd = 0;
                } else {
                    numberAdd = Integer.valueOf(snumAdd);
                }
                numberAdd = numberAdd + 1;
                if (numberAdd > 99999999) {
                    ToastUtils.showToast(GoodsDetailActivity.this, "您想输入的超过了录入最大值");
                    return;
                }
                String s = "" + numberAdd;
                mEdit_num.setText(s);
                mEdit_num.setSelection(s.length());
                break;
            case R.id.bt_sure:
                mTv_reduce.setVisibility(View.GONE);
                mTv_add.setVisibility(View.GONE);
                mEdit_num.setBackground(getResources().getDrawable(R.drawable.bg_round_frame));
                mEdit_num.setPadding(10, 5, 10, 5);
                mEdit_num.setEnabled(false);
                break;
            case R.id.bt_submit:
                Intent intent = new Intent();
                intent.putExtra("orderDetail", "1");
                setResult(resultCode, intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            giveUpThisOrder();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void giveUpThisOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GoodsDetailActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("温馨提示");
        builder.setMessage("您确定要取消这笔订单吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }
}
