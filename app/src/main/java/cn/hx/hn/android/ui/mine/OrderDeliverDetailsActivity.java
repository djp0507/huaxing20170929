package cn.hx.hn.android.ui.mine;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hx.hn.android.BaseActivity;
import cn.hx.hn.android.R;
import cn.hx.hn.android.adapter.OrderDeliverListViewAdapter;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.custom.XListView;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.RemoteDataHandler.Callback;
import cn.hx.hn.android.http.ResponseData;

/**
 * 查看物流界面
 *
 * @author KingKong-HE
 * @Time 2015-1-27
 * @Email KingKong@QQ.COM
 */
public class OrderDeliverDetailsActivity extends BaseActivity  {

    private MyShopApplication myApplication;

    private TextView deliverCodeID, deliverNameID;

    private OrderDeliverListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_deliver_view);
        MyExceptionHandler.getInstance().setContext(this);
        setCommonHeader("物流信息");
        myApplication = (MyShopApplication) getApplicationContext();
        MyExceptionHandler.getInstance().setContext(this);
        initViewID();// 初始化注册控件ID
    }

    /**
     * 初始化注册控件ID
     */
    public void initViewID() {

        String order_id = getIntent().getStringExtra("order_id");


        XListView listViewID = (XListView) findViewById(R.id.listViewID);

        deliverCodeID = (TextView) findViewById(R.id.deliverCodeID);

        deliverNameID = (TextView) findViewById(R.id.deliverNameID);

        adapter = new OrderDeliverListViewAdapter(OrderDeliverDetailsActivity.this);

        listViewID.setAdapter(adapter);

        listViewID.setPullLoadEnable(false);

        listViewID.setPullRefreshEnable(false);

        loadingDeliverData(order_id);

    }

    /**
     * 加载物流信息数据
     *
     * @param order_id 订单编号
     */
    public void loadingDeliverData(String order_id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", myApplication.getLoginKey());
        params.put("order_id", order_id);
        RemoteDataHandler.asyncLoginPostDataString(myApplication.getpath()+Constants.URL_QUERY_DELIVER, params, myApplication, new Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (data.getCode() == HttpStatus.SC_OK) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String express_name = jsonObject.getString("express_name");
                        String shipping_code = jsonObject.getString("shipping_code");
                        String deliver_info = jsonObject.getString("deliver_info");
                        deliverNameID.setText("物流公司：" + (express_name == null ? "" : express_name));
                        deliverCodeID.setText("物流编号：" + (shipping_code == null ? "" : shipping_code));
                        adapter.setjsonArray(new JSONArray(deliver_info));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    try {
                        JSONObject obj2 = new JSONObject(json);
                        String error = obj2.getString("error");
                        if (error != null) {
                            Toast.makeText(OrderDeliverDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


}
