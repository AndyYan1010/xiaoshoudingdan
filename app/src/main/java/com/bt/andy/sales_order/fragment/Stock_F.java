package com.bt.andy.sales_order.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.andy.sales_order.R;
import com.bt.andy.sales_order.myTools.DropBean;
import com.bt.andy.sales_order.myTools.DropdownButton;
import com.bt.andy.sales_order.utils.Consts;
import com.bt.andy.sales_order.utils.ProgressDialogUtil;
import com.bt.andy.sales_order.utils.SoapUtil;
import com.bt.andy.sales_order.utils.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @创建者 AndyYan
 * @创建时间 2018/7/26 8:37
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class Stock_F extends Fragment implements View.OnClickListener {
    private View                          mRootView;
    private TextView                      tv_title;
    private EditText                      edit_goods_id;
    private ImageView                     img_scan;
    private TextView                      tv_surema;
    private DropdownButton                downbt;
    private TextView                      tv_no_good;
    private TextView                      tv_name1;
    private TextView                      tv_stock;
    private List<HashMap<String, String>> mHTot;//记录模糊查询结果（商品名:商品id）
    private List<DropBean>                mGoodsNameList;//放置商品名称
    private LinearLayout                  linear_stock;//库存信息模块
    private int REQUEST_CODE                       = 1002;//接收扫描结果
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 1001;//申请照相机权限结果

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.stock_f, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        tv_title = mRootView.findViewById(R.id.tv_title);
        edit_goods_id = mRootView.findViewById(R.id.edit_goods_id);
        img_scan = mRootView.findViewById(R.id.img_scan);
        tv_surema = mRootView.findViewById(R.id.tv_surema);
        downbt = mRootView.findViewById(R.id.downbt);
        tv_no_good = mRootView.findViewById(R.id.tv_no_good);
        tv_name1 = mRootView.findViewById(R.id.tv_name1);
        linear_stock = mRootView.findViewById(R.id.linear_stock);
        tv_stock = mRootView.findViewById(R.id.tv_stock);
    }

    private void initData() {
        tv_title.setText("库存");
        tv_surema.setOnClickListener(this);
        img_scan.setOnClickListener(this);
        //模糊查询结果
        mGoodsNameList = new ArrayList<DropBean>();
        downbt.setData(mGoodsNameList);
        downbt.setOnDropItemSelectListener(new DropdownButton.OnDropItemSelectListener() {
            @Override
            public void onDropItemSelect(int Postion) {
                if (Postion == 0) {
                    ToastUtils.showToast(getContext(), "请选择商品");
                    return;
                }
                HashMap<String, String> goodsMap = mHTot.get(Postion - 1);
                String fnumber = goodsMap.get("fnumber");
                downbt.setChecked(false);
                downbt.setVisibility(View.GONE);
                //查询商品id，写入商品库存
                writeGoodsStock(fnumber);
            }
        });
        downbt.setVisibility(View.GONE);
    }

    private void writeGoodsStock(String fnumber) {
        tv_no_good.setVisibility(View.VISIBLE);
        linear_stock.setVisibility(View.GONE);
        //访问网络，获取详情
        //根据扫描的代码查询
        String sql = "select top 1 isnull(d.fprice,0) fprice,a.fitemid,a.funitid,a.fname,a.FSalePrice,isnull(sum(b.FQty),0) FQty,c.fname funit from t_icitem a left join ICInventory b on a.fitemid=b.fitemid left join t_MeasureUnit c on c.fitemid=a.FUnitID left join (select FPrice,FItemID,FBegDate from ICPrcPlyEntry ) d on a.fitemid=d.fitemid where a.fnumber='" + fnumber + "' group by a.fname,a.FSalePrice,c.fname,a.fitemid,a.funitid,d.fprice,d.FBegDate order by d.FBegDate desc";
        //根据助记码或者名称模糊查询
        new ItemTask(sql).execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_surema:
                downbt.setVisibility(View.VISIBLE);
                tv_no_good.setVisibility(View.VISIBLE);
                linear_stock.setVisibility(View.GONE);
                String goodsMid = String.valueOf(edit_goods_id.getText()).trim();
                if (null == goodsMid || "".equals(goodsMid) || "商品编码".equals(goodsMid)) {
                    ToastUtils.showToast(getContext(), "请输入商品编码");
                    return;
                }
                Task task = new Task(goodsMid);
                task.execute();
                break;
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
                    downbt.setVisibility(View.VISIBLE);
                    //获取商品信息
                    writeGoodsStock(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class Task extends AsyncTask<Void, String, String> {
        //输入框里获得
        String text;

        public Task(String text) {
            this.text = text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String sql = "select fnumber,fname from t_icitem where FHelpCode like'%" + text + "%' or fname like '%" + text + "%'";
            Map<String, String> map = new HashMap<>();
            map.put("FSql", sql);
            map.put("FTable", "t_icitem");
            return SoapUtil.requestWebService(Consts.JA_select, map);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (null == mHTot) {
                    mHTot = new ArrayList();
                } else {
                    mHTot.clear();
                }
                Document doc = DocumentHelper.parseText(s);
                Element ele = doc.getRootElement();
                Iterator iter = ele.elementIterator("Cust");
                while (iter.hasNext()) {
                    HashMap<String, String> map = new HashMap<>();
                    Element recordEle = (Element) iter.next();
                    map.put("fnumber", recordEle.elementTextTrim("fnumber"));//物料条码
                    map.put("fname", recordEle.elementTextTrim("fname"));//物料名称
                    mHTot.add(map);
                }
                //填充数据到页面
                downbt.setVisibility(View.VISIBLE);
                if (null == mGoodsNameList) {
                    mGoodsNameList = new ArrayList<>();
                } else {
                    mGoodsNameList.clear();
                }
                mGoodsNameList.add(new DropBean("请选择查询结果"));
                for (HashMap<String, String> map : mHTot) {
                    String fname = map.get("fname");
                    mGoodsNameList.add(new DropBean(fname));
                }
                downbt.setData(mGoodsNameList);
                downbt.setChecked(true);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showToast(getContext(), "未查询到类似商品助记码");
            }
        }
    }

    class ItemTask extends AsyncTask<Void, String, String> {
        String sql;

        ItemTask(String sql) {
            this.sql = sql;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialogUtil.startShow(getContext(), "正在查找,请稍等...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Map<String, String> map = new HashMap<>();
            map.put("FSql", sql);
            map.put("FTable", "t_icitem");
            return SoapUtil.requestWebService(Consts.JA_select, map);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Document doc = DocumentHelper.parseText(s);
                Element ele = doc.getRootElement();
                Iterator iter = ele.elementIterator("Cust");
                HashMap<String, String> map = new HashMap<>();
                while (iter.hasNext()) {
                    Element recordEle = (Element) iter.next();
                    map.put("itemid", recordEle.elementTextTrim("fitemid"));//物料内码(提交订单用)
                    map.put("fname", recordEle.elementTextTrim("fname"));//物料名称
                    map.put("fsaleprice", recordEle.elementTextTrim("FSalePrice"));//销售单价
                    map.put("fqty", recordEle.elementTextTrim("FQty"));//库存数量
                    map.put("funit", recordEle.elementTextTrim("funit"));//单位
                    map.put("funitid", recordEle.elementTextTrim("funitid"));//单位id
                    map.put("fprice", recordEle.elementTextTrim("fprice"));//折后单价
                }
                //填充数据到页面
                tv_no_good.setVisibility(View.GONE);
                linear_stock.setVisibility(View.VISIBLE);
                tv_name1.setText(map.get("fname"));
                double fqty = Double.parseDouble(map.get("fqty"));
                tv_stock.setText("库存数量:" + fqty + map.get("funit"));
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showToast(getContext(), "查询出错,未查到此商品");
            }
            ProgressDialogUtil.hideDialog();
        }
    }
}
