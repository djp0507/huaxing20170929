package cn.hx.hn.android.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hx.hn.android.R;
import cn.hx.hn.android.adapter.Merchantsadapter;
import cn.hx.hn.android.bean.Store_list;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.SaveSp;
import cn.hx.hn.android.common.ShopHelper;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.ResponseData;
import cn.hx.hn.android.utils.DebugUtils;
import cn.hx.hn.android.utils.SpUtils;

/**
 * 附近店铺列表
 * Created by Administrator on 2016/12/9.
 */

public class MerchantsFragment extends Fragment {
    private MyShopApplication myApplication;
    private Gson gson;

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rv_list;
    private Merchantsadapter adapter;

    private ArrayList<Store_list> infos = new ArrayList<>();
    private int curpage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_merchants, container, false);
        MyExceptionHandler.getInstance().setContext(getActivity());
        myApplication = (MyShopApplication) getActivity().getApplicationContext();
        gson = new Gson();

        initview(layout);
        return layout;
    }

    private void initview(View v) {
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeLayout);
        rv_list = (RecyclerView) v.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new Merchantsadapter(infos);
        //设置空布局
        View view = View.inflate(getActivity(), R.layout.app_list_empty, null);
        LinearLayout lin = (LinearLayout) view.findViewById(R.id.llListEmpty);
        lin.setVisibility(View.VISIBLE);
        ImageView ivListEmpty = (ImageView) view.findViewById(R.id.ivListEmpty);
        ivListEmpty.setImageResource(R.drawable.nc_icon_order);
        TextView tvListEmptyTitle = (TextView) view.findViewById(R.id.tvListEmptyTitle);
        TextView tvListEmptySubTitle = (TextView) view.findViewById(R.id.tvListEmptySubTitle);
        tvListEmptyTitle.setText("还没有相关的店铺");
        tvListEmptySubTitle.setText("可以去别处逛逛");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);

        adapter.setEmptyView(view);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rv_list.setAdapter(adapter);

        SwipeRefreshLayout.OnRefreshListener on = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curpage = 1;
                LoadDate();
            }
        };
        swipeLayout.setOnRefreshListener(on);
        adapter.setEnableLoadMore(false);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curpage++;
                LoadDate();
            }
        });
        rv_list.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                Intent intent = new Intent(getActivity(), newStoreInFoActivity.class);
                intent.putExtra("store_id", infos.get(position).getStore_id());
                startActivity(intent);
            }
        });

        on.onRefresh();
    }

    private void LoadDate() {
        String url = Constants.URL_STORE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", MyShopApplication.getInstance().getLoginKey());
        params.put("curpage", curpage + "");
        DebugUtils.printLogD(curpage + "");
        String lat = SpUtils.getSpString(getActivity(), SaveSp.LATITUDE);
        String lng = SpUtils.getSpString(getActivity(), SaveSp.LONGITUDE);
        if (lat.equals("0") && lng.equals("0")) {

        } else {
            params.put("lat", lat);
            params.put("lng", lng);
        }

        RemoteDataHandler.asyncLoginPostDataString(url, params, MyShopApplication.getInstance(), new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                swipeLayout.setRefreshing(false);
                adapter.loadMoreComplete();
                String json = data.getJson();
                Logger.d(data.getJson());
                if (data.getCode() == HttpStatus.SC_OK) {
                    try {

                        JSONObject jsonObject = new JSONObject(json);
                        Type listType = new TypeToken<List<Store_list>>() {
                        }.getType();
                        ArrayList<Store_list> store_lists = gson.fromJson(jsonObject.getString("store_list"), listType);
                        if (curpage == 1) {
                            infos.clear();
                            adapter.setNewData(store_lists);
                        } else {
                            adapter.addData(store_lists);
                        }
                        infos.addAll(store_lists);
                        if (store_lists.size() >= 10) {
                            adapter.setEnableLoadMore(true);
                        } else {
                            adapter.setEnableLoadMore(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShopHelper.showApiError(getActivity(), json);
                }
            }
        });
    }


}
