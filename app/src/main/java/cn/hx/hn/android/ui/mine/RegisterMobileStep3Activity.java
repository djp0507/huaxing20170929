package cn.hx.hn.android.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hx.hn.android.BaseActivity;
import cn.hx.hn.android.R;
import cn.hx.hn.android.bean.Login;
import cn.hx.hn.android.bean.Redpacket_templateInfo;
import cn.hx.hn.android.bean.Voucher_templateInfo;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.ShopHelper;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.ResponseData;
import cn.hx.hn.android.utils.DebugUtils;

public class RegisterMobileStep3Activity extends BaseActivity {

    private MyShopApplication myApplication;
    private EditText etPassword;
    private ImageButton btnShowPassowrd;
    private Button btnRegSubmit;

    private String phone;
    private String captcha;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile_step3);
        MyExceptionHandler.getInstance().setContext(this);
        gson = new Gson();
        phone = getIntent().getStringExtra("phone");
        captcha = getIntent().getStringExtra("captcha");

        myApplication = (MyShopApplication) getApplicationContext();
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnShowPassowrd = (ImageButton) findViewById(R.id.btnShowPassword);
        btnRegSubmit = (Button) findViewById(R.id.btnRegSubmit);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etPassword.getText().toString().length() > 0) {
                    btnRegSubmit.setActivated(true);
                } else {
                    btnRegSubmit.setActivated(false);
                }
            }
        });

        setCommonHeader("设置密码");
    }

    /**
     * 显示密码按钮点击
     */
    public void btnShowPasswordClick(View v) {
        if (btnShowPassowrd.isSelected()) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnShowPassowrd.setSelected(false);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            btnShowPassowrd.setSelected(true);
        }
    }

    /**
     * 注册按钮点击
     */
    public void btnRegSubmitClick(View v) {
        if (btnRegSubmit.isActivated()) {
            String password = etPassword.getText().toString();
            int length = password.length();
            if (length < 6 || length > 20) {
                ShopHelper.showMessage(RegisterMobileStep3Activity.this, "请输入6-20位密码");
                return;
            }

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("phone", phone);
            params.put("captcha", captcha);
            params.put("password", password);
            params.put("client", "android");
            RemoteDataHandler.asyncPostDataString(Constants.URL_CONNECT_SMS_REGISTER, params, new RemoteDataHandler.Callback() {
                @Override
                public void dataLoaded(ResponseData data) {
                    String json = data.getJson();
                    if (data.getCode() == HttpStatus.SC_OK) {

                        Login login = Login.newInstanceList(json);
                        myApplication.setLoginKey(login.getKey());
                        myApplication.setUserName(login.getUsername());
                        myApplication.setMemberID(login.getUserid());

                        myApplication.loadingUserInfo(login.getKey(), login.getUserid());

                        myApplication.getmSocket().connect();
                        myApplication.UpDateUser();

                        Intent mIntent = new Intent(Constants.LOGIN_SUCCESS_URL);
                        sendBroadcast(mIntent);

                        //查看接口是否返回赠送红包或者优惠券
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String isgiving = jsonObject.optString("isgiving");
                            if (TextUtils.isEmpty(isgiving)) {
                                finish();
                            }
                            JSONObject isgiving_obj = new JSONObject(isgiving);
                            int type = isgiving_obj.optInt("type");
                            switch (type) {
                                case 1:
                                    //红包
                                    if (isgiving_obj.optBoolean("msg")){
                                        String info = isgiving_obj.optString("info");
                                        DebugUtils.printLogD(info);

                                        Redpacket_templateInfo redpacket_templateInfo = gson.fromJson(info, Redpacket_templateInfo.class);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMobileStep3Activity.this);
                                        builder.setTitle("选择");
                                        builder.setMessage("恭喜您获得"+redpacket_templateInfo.getRpacket_t_price()+"元红包");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                RegisterMobileStep3Activity.this.finish();
                                            }
                                        });
                                        builder.setNeutralButton("确定",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(RegisterMobileStep3Activity.this, RedpacketListActivity.class));
                                            }
                                        });
                                        builder.create().show();
                                    }else{
                                        RegisterMobileStep3Activity.this.finish();
                                    }
                                    break;
                                case 2:
                                    //优惠券
                                    if (isgiving_obj.optBoolean("msg")){
                                        String info = isgiving_obj.optString("info");
                                        DebugUtils.printLogD(info);
                                        Voucher_templateInfo voucher_templateInfo = gson.fromJson(info, Voucher_templateInfo.class);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMobileStep3Activity.this);
                                        builder.setTitle("选择");
                                        builder.setMessage("恭喜您获得"+voucher_templateInfo.getVoucher_price()+"元代金券");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        builder.setNeutralButton("确定",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(RegisterMobileStep3Activity.this, VoucherListActivity.class));
                                            }
                                        });
                                        builder.create().show();
                                    }else{
                                        RegisterMobileStep3Activity.this.finish();
                                    }
                                    break;
                                default:
                                    RegisterMobileStep3Activity.this.finish();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        ShopHelper.showApiError(RegisterMobileStep3Activity.this, json);
                    }
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_mobile_step3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
