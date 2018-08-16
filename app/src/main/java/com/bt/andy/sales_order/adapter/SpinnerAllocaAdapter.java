package com.bt.andy.sales_order.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bt.andy.sales_order.R;
import com.bt.andy.sales_order.messegeInfo.AllocationInfo;

import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/8/16 16:58
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */

public class SpinnerAllocaAdapter extends BaseAdapter {
    private Context              mContext;
    private List<AllocationInfo> mList;

    public SpinnerAllocaAdapter(Context context, List<AllocationInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder viewHolder;
        if (null == view) {
            viewHolder = new MyViewHolder();
            view = View.inflate(mContext, R.layout.spinner_item_kind, null);
            viewHolder.tv_kind = view.findViewById(R.id.tv_kind);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        viewHolder.tv_kind.setText(mList.get(i).getFName());
        return view;
    }

    private class MyViewHolder {
        TextView tv_kind;
    }
}
