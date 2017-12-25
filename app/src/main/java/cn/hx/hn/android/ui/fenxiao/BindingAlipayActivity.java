package cn.hx.hn.android.ui.fenxiao;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hx.hn.android.BaseActivity;
import cn.hx.hn.android.R;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.ResponseData;

import static cn.hx.hn.android.ui.fenxiao.ShareLibListener.url;

public class BindingAlipayActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_number;
    private TextView tv_name;
    private TextView tv_number;
    private LinearLayout ll_isShow;
    private int type;
    private MyShopApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_card);
        initUI();
    }

    private void initUI() {
        myApplication = MyShopApplication.getInstance();
        type = getIntent().getIntExtra("type", 0);

        //收款人名字
        et_name = (EditText) findViewById(R.id.edit_binding_name_card);
        checkHintSize(et_name,"请输入支付宝姓名");
        //银行卡号
        et_number = (EditText) findViewById(R.id.edit_binding_number_card);
        checkHintSize(et_number,"请输入支付宝账号");


        tv_name = (TextView) findViewById(R.id.tv_binding_name_card);
        tv_number = (TextView) findViewById(R.id.tv_binding_number_card);

        Button btn_isBinding = (Button) findViewById(R.id.btn_isBinding);
        btn_isBinding.setOnClickListener(this);

        ll_isShow = (LinearLayout) findViewById(R.id.ll_binding);
        if (type == 0) { //当type为0时，则处理绑定银行卡事件
            setCommonHeader(getString(R.string.binding_alipay));
            et_name.setVisibility(View.VISIBLE);
            et_number.setVisibility(View.VISIBLE);
            btn_isBinding.setText(R.string.binding);
        } else if (type == 1) { //当type为1时，则处理解绑银行卡事件
            String name = getIntent().getStringExtra("name");
            String number = getIntent().getStringExtra("number");
            setCommonHeader(getString(R.string.unbinding_alipay));
            tv_name.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
            tv_name.setText(name);
            tv_number.setText(number);
            btn_isBinding.setText(R.string.unbinding);
        }
    }

    private void checkHintSize(EditText et_name, String hint) {
        SpannableString s = new SpannableString(hint);
        AbsoluteSizeSpan textSize = new AbsoluteSizeSpan(16,true);
        s.setSpan(textSize,0,s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_name.setHint(s);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_isBinding:
                if (type == 0) {  //绑定
                    String url = Constants.URL_BINDING_ALIPAY_OR_BANK;
                    String name = et_name.getText().toString(); //支付宝名字
                    String number = et_number.getText().toString(); //支付宝卡号
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("key", myApplication.getLoginKey());
                    params.put("name", name);
                    params.put("account", number);
                    params.put("type", "1");

                    RemoteDataHandler.asyncPostDataString(url, params, new RemoteDataHandler.Callback() {
                        @Override
                        public void dataLoaded(ResponseData data) {
                            String json = data.getJson();
                            if (data.getCode() == HttpStatus.SC_OK) {
                                try {
                                    JSONObject obj = new JSONObject(json);
                                    String msg = obj.getString("msg");
                                    Toast.makeText(BindingAlipayActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    BindingAlipayActivity.this.finish();
                                } catch (Exception e) {
                                    Toast.makeText(BindingAlipayActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else if (type == 1) { //解绑

                    url = Constants.URL_UNBINDING_ALIPAY_OR_BANK;
                    String name = et_name.getText().toString(); //收款名字
                    String number = et_number.getText().toString(); //银行卡号
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("key", myApplication.getLoginKey());


                    RemoteDataHandler.asyncPostDataString(url, params, new RemoteDataHandler.Callback() {
                        @Override
                        public void dataLoaded(ResponseData data) {
                            String json = data.getJson();
                            if (data.getCode() == HttpStatus.SC_OK) {
                                try {
                                    JSONObject obj = new JSONObject(json);
                                    String msg = obj.getString("msg");
                                    Toast.makeText(BindingAlipayActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    BindingAlipayActivity.this.finish();
                                } catch (Exception e) {
                                    Toast.makeText(BindingAlipayActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    break;
                }
        }
    }

}
