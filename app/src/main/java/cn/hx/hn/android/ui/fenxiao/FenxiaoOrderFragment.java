package cn.hx.hn.android.ui.fenxiao;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;

import cn.hx.hn.android.R;
import cn.hx.hn.android.bean.FenxiaoOrderlist;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.JsonUtil;
import cn.hx.hn.android.common.LoadImage;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.ShopHelper;
import cn.hx.hn.android.common.SwpipeListViewOnScrollListener;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.ResponseData;
import cn.hx.hn.android.utils.DebugUtils;

/**
 * Created by snm on 2016/9/22.
 */
public class FenxiaoOrderFragment extends Fragment{

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeRefreshLayout.OnRefreshListener listener;
    public String states;
    FenxiaoOrderAdapter adapter;
    public static FenxiaoOrderFragment getInstance(String states) {
        FenxiaoOrderFragment sf = new FenxiaoOrderFragment();
        sf.states = states;
        return sf;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fenxiao_settlement_listview, null);

        initView(v);
        setListEmpty(v,R.drawable.nc_icon_order, "您还没有相关订单", "可以去看看哪些想要买的");
        return v;
    }
    public void initView(View view){
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        listView = (ListView)view.findViewById(R.id.mylistview);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);


        listener = new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh(){
                LoadDate();
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);


        swipeRefreshLayout.post(new Runnable(){
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        listener.onRefresh();
        adapter = new FenxiaoOrderAdapter(getActivity());
        listView.setOnScrollListener(new SwpipeListViewOnScrollListener(swipeRefreshLayout));
    }
    private void LoadDate() {
        String url = Constants.URL_DISTRI_ORDER+"&page=0";
        DebugUtils.printLogD(url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", MyShopApplication.getInstance().getLoginKey());
        DebugUtils.printLogD("key="+MyShopApplication.getInstance().getLoginKey());
        DebugUtils.printLogD("state_type="+states);
        params.put("goods_name", "");
        params.put("state_type", states);
        Logger.d(params.toString());
        RemoteDataHandler.asyncLoginPostDataString(url, params, MyShopApplication.getInstance(), new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {

                String json = data.getJson();
                Logger.d(data.getJson());
                if (data.getCode() == HttpStatus.SC_OK) {
                    FenxiaoOrderlist orderlist = JsonUtil.getBean(json,FenxiaoOrderlist.class);
                    if(orderlist.getOrder_list().isEmpty()){
                        showListEmpty();
                    }else {
                        hideListEmpty();
                        adapter.setList(orderlist.getOrder_list());
                        listView.setAdapter(adapter);
                    }
                } else {
                    showListEmpty();
                    ShopHelper.showApiError(getActivity(), json);
                }
                stopRefreshing();
            }
        });
    }
    public void stopRefreshing(){
        swipeRefreshLayout.post(new Runnable(){
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    public class FenxiaoOrderAdapter extends BaseAdapter{

        ArrayList<FenxiaoOrderlist.OrderListBean> list;
        private LayoutInflater inflater;
        private Context mContext;
        private FenxiaoOrderAdapter(Context c){
            this.mContext = c;
            this.inflater = LayoutInflater.from(c);
        }

        public void setList(ArrayList<FenxiaoOrderlist.OrderListBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list == null?0:list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.fragment_listivew_order_item, null);
                holder = new ViewHolder();
                holder.textGoodsName = (TextView) convertView.findViewById(R.id.textGoodsName);
                holder.textGoodsPrice = (TextView) convertView.findViewById(R.id.textGoodsPrice);
                holder.textGoodsJingle = (TextView) convertView.findViewById(R.id.textGoodsJingle);
                holder.textGoodsState = (TextView) convertView.findViewById(R.id.textGoodsState);
                holder.textGoodsyong = (TextView) convertView.findViewById(R.id.textGoodsyong);
                holder.textGoodsNUM = (TextView) convertView.findViewById(R.id.textGoodsNUM);
                holder.imageGoodsPic = (ImageView)convertView.findViewById(R.id.imageGoodsPic);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            FenxiaoOrderlist.OrderListBean orderlist = list.get(position);
            LoadImage.loadImg(mContext,holder.imageGoodsPic,orderlist.getGoods_image_url());
            holder.textGoodsName.setText(orderlist.getGoods_name());
            holder.textGoodsPrice.setText("￥"+orderlist.getGoods_price());
            holder.textGoodsJingle.setText(orderlist.getStore_name());
            holder.textGoodsState.setText(orderlist.getOrder_state_txt());
            holder.textGoodsyong.setText("佣金￥"+orderlist.getDis_commis_amount());
            holder.textGoodsNUM.setText("x"+orderlist.getGoods_num());

            return convertView;
        }

        class ViewHolder {
            TextView textGoodsName,textGoodsJingle,textGoodsState,textGoodsyong,textGoodsNUM;
            TextView textGoodsPrice;
            ImageView imageGoodsPic;
        }
    }
    private LinearLayout llListEmpty;
    /**
     * 空列表背景
     */
    protected void setListEmpty(View v,int resId, String title, String subTitle) {
        llListEmpty = (LinearLayout) v.findViewById(R.id.llListEmpty);
        ImageView ivListEmpty = (ImageView) v.findViewById(R.id.ivListEmpty);
        ivListEmpty.setImageResource(resId);
        TextView tvListEmptyTitle = (TextView) v.findViewById(R.id.tvListEmptyTitle);
        TextView tvListEmptySubTitle = (TextView) v.findViewById(R.id.tvListEmptySubTitle);
        tvListEmptyTitle.setText(title);
        tvListEmptySubTitle.setText(subTitle);
    }

    /**
     * 显示空列表背景
     */
    protected void showListEmpty() {
        llListEmpty.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏空列表背景
     */
    protected void hideListEmpty() {
        llListEmpty.setVisibility(View.GONE);
    }


}
