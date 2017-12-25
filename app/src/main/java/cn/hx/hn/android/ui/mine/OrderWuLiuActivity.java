package cn.hx.hn.android.ui.mine;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hx.hn.android.BaseActivity;
import cn.hx.hn.android.R;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.ResponseData;


public class OrderWuLiuActivity extends BaseActivity {
    private MyShopApplication myApplication;
    private TextView textWuLiuCompany;
    private TextView textWuLiuId;
    private TextView textWuLiuInfo;
    private String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_wu_liu);
        MyExceptionHandler.getInstance().setContext(this);
        setCommonHeader("物流信息");
        myApplication = (MyShopApplication) getApplicationContext();
        orderId=getIntent().getStringExtra("order_id");
        initViews();
        loadWuLiuInfo();
    }

    public void initViews(){
        textWuLiuCompany=(TextView)findViewById(R.id.textWuLiuCompany);
        textWuLiuId=(TextView)findViewById(R.id.textWuLiuId);
        textWuLiuInfo=(TextView)findViewById(R.id.textWuLiuInfo);
    }

    public void loadWuLiuInfo(){
        String url= myApplication.getpath()+Constants.URL_ORDER_WULIU_INFO;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", myApplication.getLoginKey());
        params.put("order_id", orderId);
        Log.i("QIN",orderId);
        RemoteDataHandler.asyncLoginPostDataString(url,params,myApplication,new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json=data.getJson();
                Log.i("QIN",json);
                if(data.getCode()== HttpStatus.SC_OK){
                    try{
                        JSONObject obj=new JSONObject(json);
                        textWuLiuCompany.setText(obj.getString("express_name"));
                        textWuLiuId.setText(obj.getString("shipping_code"));
                        textWuLiuInfo.setText(obj.getString("deliver_info"));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }else{
//                    ShopHelper.showApiError(OrderWuLiuActivity.this, json);
                }
            }
        });
    }


}
