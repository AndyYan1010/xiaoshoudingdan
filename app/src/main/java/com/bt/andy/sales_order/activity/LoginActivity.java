package com.bt.andy.sales_order.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bt.andy.sales_order.BaseActivity;
import com.bt.andy.sales_order.MainActivity;
import com.bt.andy.sales_order.R;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/22 9:05
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEdit_num;
    private EditText mEdit_psd;
    private Button   mBt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_actiivty);
        getView();
        setData();
    }

    private void getView() {
        mEdit_num = (EditText) findViewById(R.id.edit_num);
        mEdit_psd = (EditText) findViewById(R.id.edit_psd);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void setData() {
        mBt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
