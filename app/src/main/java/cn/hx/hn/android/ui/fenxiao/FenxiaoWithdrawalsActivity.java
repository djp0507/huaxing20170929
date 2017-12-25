package cn.hx.hn.android.ui.fenxiao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ta.utdid2.android.utils.StringUtils;

import org.apache.http.HttpStatus;

import java.util.HashMap;

import cn.hx.hn.android.BaseActivity;
import cn.hx.hn.android.R;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.ShopHelper;
import cn.hx.hn.android.common.T;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.ResponseData;

/**
 * Created by snm on 2016/9/23.
 */
public class FenxiaoWithdrawalsActivity extends BaseActivity {

    private TextView tv_keyong,tv_box_applay;
    private EditText etjiner,etpass;
    private String price = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals_main);
        Intent intent = getIntent();
        price = intent.getStringExtra("price");

        setCommonHeader("申请提现");
        initView();
    }
    public void initView(){
        tv_keyong = (TextView)findViewById(R.id.tv_keyong);
        tv_box_applay = (TextView)findViewById(R.id.tv_box_applay);
        etjiner = (EditText) findViewById(R.id.etjiner);
        etpass = (EditText)findViewById(R.id.etpass);
        tv_box_applay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jiner = etjiner.getText().toString().trim();
                String pass = etpass.getText().toString().trim();
                if(isImplay(jiner,pass)){
                    cash_apply(jiner,pass);
                }
            }
        });
        tv_keyong.setText("可提现金额 ￥" + price);
    }
    public Boolean isImplay(String jiner,String pass){
        if(StringUtils.isEmpty(jiner)){
            T.showShort(getApplicationContext(),"输入的金额不能为空");
            return false;
        }
        if(StringUtils.isEmpty(pass)){
            T.showShort(getApplicationContext(),"输入的密码不能为空");
            return false;
        }
        return true;
    }
    public void cash_apply(String jiner,String pass){
        String url = Constants.URL_CASH_APPLY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", MyShopApplication.getInstance().getLoginKey());
        params.put("cash_amount", jiner);
        params.put("pay_pwd", pass);

        Log.d("key",MyShopApplication.getInstance().getLoginKey());
        Log.d("cash_amount",jiner);
        Log.d("pay_pwd",pass);

        RemoteDataHandler.asyncLoginPostDataString(url, params, MyShopApplication.getInstance(), new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {

                String json = data.getJson();
                Log.d("TAG83","json="+json);
                Logger.d(data.toString());
                if (data.getCode() == HttpStatus.SC_OK) {


//                    if("1".equals(json)){
                    if(json.indexOf("success")!=-1){
                        T.showShort(getApplicationContext(),"提现成功");
                        finish();
                    }else {
                        T.showShort(getApplicationContext(),"提现失败");
                    }
                } else {
                    ShopHelper.showApiError(getApplicationContext(), json);
                }
            }
        });
    }

}
