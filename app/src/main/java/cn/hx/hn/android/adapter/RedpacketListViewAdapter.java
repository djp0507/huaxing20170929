package cn.hx.hn.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.hx.hn.android.R;
import cn.hx.hn.android.bean.RedpacketInfo;
import cn.hx.hn.android.common.ShopHelper;

import java.util.ArrayList;

/**
 * 红包适配器
 *
 * dqw
 * 2015/8/27
 */
public class RedpacketListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<RedpacketInfo> redpacketInfoArrayList;

    public RedpacketListViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return redpacketInfoArrayList == null ? 0 : redpacketInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return redpacketInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(ArrayList<RedpacketInfo> redpacketInfoArrayList) {
        this.redpacketInfoArrayList = redpacketInfoArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listivew_redpacket_item, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            holder.tvCode= (TextView) convertView.findViewById(R.id.tvCode);
            holder.priceID = (TextView) convertView.findViewById(R.id.priceID);
            holder.manID = (TextView) convertView.findViewById(R.id.manID);
            holder.endTimeID = (TextView) convertView.findViewById(R.id.endTimeID);
            holder.main = (RelativeLayout)convertView.findViewById(R.id.redpachket_ll_main);
            holder.isused = (ImageView) convertView.findViewById(R.id.isused);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RedpacketInfo bean = redpacketInfoArrayList.get(position);

        ShopHelper.loadImage(holder.ivImage, bean.getCustomimgUrl());
        holder.tvCode.setText(bean.getCode());
        holder.priceID.setText("￥" + bean.getPrice());
        holder.manID.setText("满" + bean.getLimit() + "可用");
        holder.endTimeID.setText("有效期至：" + bean.getEndDateText());
        if("unused".equals(bean.getStateKey())){
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.app_red));
            holder.isused.setVisibility(View.GONE);
        }
        if("used".equals(bean.getStateKey())){
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.app_gray));
            holder.isused.setVisibility(View.VISIBLE);
            holder.isused.setImageResource(R.drawable.yishiyong);
        }

        if("expire".equals(bean.getStateKey())){
            holder.main.setBackgroundColor(context.getResources().getColor(R.color.app_gray));
            holder.isused.setVisibility(View.VISIBLE);
            holder.isused.setImageResource(R.drawable.yishixiao);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView ivImage,isused;
        TextView tvCode, priceID, manID, endTimeID;
        RelativeLayout main;
    }
}
