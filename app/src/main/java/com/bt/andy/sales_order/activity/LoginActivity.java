package com.bt.andy.sales_order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bt.andy.sales_order.MainActivity;
import com.bt.andy.sales_order.MyAppliaction;
import com.bt.andy.sales_order.R;
import com.bt.andy.sales_order.utils.Consts;
import com.bt.andy.sales_order.utils.ProgressDialogUtil;
import com.bt.andy.sales_order.utils.SoapUtil;
import com.bt.andy.sales_order.utils.SpUtils;
import com.bt.andy.sales_order.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建者 AndyYan
 * @创建时间 2018/5/22 9:05
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText mEdit_num;
    private EditText mEdit_psd;
    private CheckBox ck_remPas;
    private Button   mBt_submit;
    private boolean isRem = false;//记录是否保存账号密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_actiivty);
        MyAppliaction.flag = 0;
        getView();
        setData();
    }

    private void getView() {
        mEdit_num = (EditText) findViewById(R.id.edit_num);
        mEdit_psd = (EditText) findViewById(R.id.edit_psd);
        ck_remPas = (CheckBox) findViewById(R.id.ck_remPas);
        mBt_submit = (Button) findViewById(R.id.bt_login);
    }

    private void setData() {
        Boolean isRemem = SpUtils.getBoolean(LoginActivity.this, "isRem", false);
        if (isRemem) {
            isRem = true;
            ck_remPas.setChecked(true);
            String name = SpUtils.getString(LoginActivity.this, "name");
            String psd = SpUtils.getString(LoginActivity.this, "psd");
            mEdit_num.setText(name);
            mEdit_psd.setText(psd);
        }
        ck_remPas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRem = b;
            }
        });
        mBt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                String number = mEdit_num.getText().toString().trim();
                String pass = mEdit_psd.getText().toString().trim();
                if ("".equals(number) || "请输入工号".equals(number)) {
                    ToastUtils.showToast(LoginActivity.this, "请输入工号");
                    return;
                }
                if ("请输入密码".equals(pass)) {
                    //ToastUtils.showToast(LoginActivity.this,"请输入密码");
                    pass = "";
                }
                //是否记住账号密码
                isNeedRem(number, pass);
                new LoginTask(number, pass).execute();
                //                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //                startActivity(intent);
                break;
        }
    }

    private void isNeedRem(String name, String psd) {
        SpUtils.putBoolean(LoginActivity.this, "isRem", isRem);
        if (isRem) {
            SpUtils.putString(LoginActivity.this, "name", name);
            SpUtils.putString(LoginActivity.this, "psd", psd);
        }
    }

    class LoginTask extends AsyncTask<Void, String, String> {
        String username;
        String password;

        LoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialogUtil.startShow(LoginActivity.this, "正在登录，请稍后...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Map<String, String> map = new HashMap<>();
            map.put("UserName", username);
            map.put("PassWord", password);
            return SoapUtil.requestWebService(Consts.Login, map);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressDialogUtil.hideDialog();
            if (s.contains("成功")) {
                String[] split = s.split("/");
                String sId = split[0];
                String userid = sId.substring(2, sId.length());
                MyAppliaction.userID = userid;//用户id
                MyAppliaction.memID = username;//工号
                if (split.length >= 2) {
                    MyAppliaction.userName = split[1];//用户姓名
                } else {
                    MyAppliaction.userName = "";//用户姓名
                    ToastUtils.showToast(LoginActivity.this, "系统中未填写姓名");
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                ToastUtils.showToast(LoginActivity.this, "登陆成功");
                finish();
            } else {
                ToastUtils.showToast(LoginActivity.this, "登陆失败");
            }
        }
    }
}
