package cn.hx.hn.android.ui.type;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.hx.hn.android.R;
import cn.hx.hn.android.adapter.GoodsListViewAdapter;
import cn.hx.hn.android.adapter.GoodsListViewPiFaAdapter;
import cn.hx.hn.android.bean.GoodsList;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.custom.XListView;
import cn.hx.hn.android.custom.XListView.IXListViewListener;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.RemoteDataHandler.Callback;
import cn.hx.hn.android.http.ResponseData;

/**
 * 商品列表Fragment
 *
 * @author dqw
 * @Time 2015-7-10
 */
public class GoodsListFragment extends Fragment implements IXListViewListener {

    public String url;
    public int pageno = 1;

    private GoodsListViewAdapter goodsListViewAdapter;

    private Handler mXLHandler;

    private XListView listViewID;

    private ArrayList<GoodsList> goodsLists;

    private TextView tvNoResult;
    private RelativeLayout rl_main_list;
    private Button top_btn;
    private GoodsListViewPiFaAdapter goodsListViewpifaAdapter;
    public int type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewLayout = inflater.inflate(R.layout.goods_fragment_list, container, false);
        MyExceptionHandler.getInstance().setContext(getActivity());
        initViewID(viewLayout);//注册控件ID

        return viewLayout;
    }

    /**
     * 初始化注册控件ID
     */
    public void initViewID(View view) {

        listViewID = (XListView) view.findViewById(R.id.listViewID);
        rl_main_list = (RelativeLayout) view.findViewById(R.id.rl_main_list);
        top_btn = (Button) view.findViewById(R.id.top_btn);

        goodsLists = new ArrayList<GoodsList>();

        if (type == 1) {
            goodsListViewpifaAdapter = new GoodsListViewPiFaAdapter(getActivity(), "list");
            loadingGoodsListData1();
            listViewID.setAdapter(goodsListViewpifaAdapter);

        } else {
            goodsListViewAdapter = new GoodsListViewAdapter(getActivity(), "list");
            loadingGoodsListData();
            listViewID.setAdapter(goodsListViewAdapter);
        }

        listViewID.setXListViewListener(this);
        listViewID.setPullRefreshEnable(false);//禁止下拉刷新
        mXLHandler = new Handler();
        listViewID.setOnScrollListener(new XListView.OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                int top = absListView.getTop();
                int scrollheight = -top + i * absListView.getHeight();
                if (scrollheight >= rl_main_list.getBottom()) {
                    top_btn.setVisibility(View.VISIBLE);
                } else {
                    top_btn.setVisibility(View.GONE);
                }
            }
        });

        tvNoResult = (TextView) view.findViewById(R.id.tvNoResult);

        top_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                top_btn.setVisibility(View.GONE);
                listViewID.setSelectionFromTop(0, 0);
            }
        });
    }
    public void setType() {

    }
    private void loadingGoodsListData1() {
        url = url + "&page=" + Constants.PAGESIZE + "&curpage=" + pageno;
        Logger.d(url);
        RemoteDataHandler.asyncDataStringGet(url, new Callback() {
            @Override
            public void dataLoaded(ResponseData data) {

                listViewID.stopLoadMore();

                if (data.getCode() == HttpStatus.SC_OK) {

                    String json = data.getJson();

                    if (!data.isHasMore()) {
                        listViewID.setPullLoadEnable(false);
                    } else {
                        listViewID.setPullLoadEnable(data.isHasMore());
                    }
                    if (pageno == 1) {
                        goodsLists.clear();
                    }

                    try {

                        JSONObject obj = new JSONObject(json);
                        String array = obj.getString("goods_list");
                        if (array != "" && !array.equals("array") && array != null && !array.equals("[]")) {
                            ArrayList<GoodsList> list = GoodsList.newInstanceList1(array);
                            goodsLists.addAll(list);
                            goodsListViewpifaAdapter.setGoodsLists(goodsLists);
                            goodsListViewpifaAdapter.notifyDataSetChanged();
                        } else {
                            if (pageno == 1) {
                                tvNoResult.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.load_error, Toast.LENGTH_SHORT).show();
                    ;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        //下拉刷新
    }

    @Override
    public void onLoadMore() {
        //上拉加载
        mXLHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageno = pageno + 1;
                loadingGoodsListData();
            }
        }, 1000);
    }

    /**
     * 初始化加载列表数据
     */
    public void loadingGoodsListData() {

        url = url + "&curpage=" + pageno + "&page=" + Constants.PAGESIZE;
        Logger.d(url);
        RemoteDataHandler.asyncDataStringGet(url, new Callback() {
            @Override
            public void dataLoaded(ResponseData data) {

                listViewID.stopLoadMore();

                if (data.getCode() == HttpStatus.SC_OK) {

                    String json = data.getJson();

                    if (!data.isHasMore()) {
                        listViewID.setPullLoadEnable(false);
                    } else {
                        listViewID.setPullLoadEnable(data.isHasMore());
                    }
                    if (pageno == 1) {
                        goodsLists.clear();
                    }

                    try {

                        JSONObject obj = new JSONObject(json);
                        String array = obj.getString("goods_list");
                        if (array != "" && !array.equals("array") && array != null && !array.equals("[]")) {
                            ArrayList<GoodsList> list = GoodsList.newInstanceList(array);
                            goodsLists.addAll(list);
                            goodsListViewAdapter.setGoodsLists(goodsLists);
                            goodsListViewAdapter.notifyDataSetChanged();
                        } else {
                            if (pageno == 1) {
                                tvNoResult.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.load_error, Toast.LENGTH_SHORT).show();
                    ;
                }
            }
        });
    }
}
