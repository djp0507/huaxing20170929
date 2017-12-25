package cn.hx.hn.android.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
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
import cn.hx.hn.android.common.T;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.RemoteDataHandler.Callback;
import cn.hx.hn.android.http.ResponseData;
import cn.hx.hn.android.utils.DebugUtils;

/**
 * 用户注册
 *
 * @author dqw
 * @Time 2015-6-25
 */
public class RegisteredActivity extends BaseActivity {

    private EditText editUserName, editPassword, editPasswordConfirm, editEmail;
    private Button btnReg, btnRegMb, btnRegSubmit;
    private ImageButton btnAgree;

    private MyShopApplication myApplication;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_view);
        MyExceptionHandler.getInstance().setContext(this);
        myApplication = (MyShopApplication) RegisteredActivity.this.getApplication();
        gson = new Gson();

        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPasswordConfirm = (EditText) findViewById(R.id.editPasswordConfirm);
        editEmail = (EditText) findViewById(R.id.editEmail);

        btnReg = (Button) findViewById(R.id.btnReg);
        btnRegMb = (Button) findViewById(R.id.btnRegMb);
        btnRegSubmit = (Button) findViewById(R.id.btnRegSubmit);
        btnAgree = (ImageButton) findViewById(R.id.btnAgree);

        btnReg.setActivated(true);
        btnRegMb.setActivated(false);
        btnAgree.setSelected(true);
        btnRegSubmit.setActivated(true);

        //检查是否开启手机注册
        String url = Constants.URL_CONNECT_STATE + "&t=connect_sms_reg";
        RemoteDataHandler.asyncDataStringGet(url, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                if (data.getCode() == HttpStatus.SC_OK) {
                    LinearLayout llRegTab = (LinearLayout) findViewById(R.id.llRegTab);

                    String result = data.getJson();
                    if (result.equals("1")) {
                        llRegTab.setVisibility(View.VISIBLE);
                    } else {
                        llRegTab.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(RegisteredActivity.this, R.string.load_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        setCommonHeader("会员注册");
    }

    /*
    * 手机注册按钮
    */
    public void btnRegMbClick(View v) {
        Intent intent = new Intent(RegisteredActivity.this, RegisterMobileActivity.class);
        startActivity(intent);
        finish();
    }

    /*
     * 同意协议按钮
     */
    public void btnAgreeClick(View v) {
        if (btnAgree.isSelected()) {
            btnAgree.setSelected(false);
            btnRegSubmit.setActivated(false);
        } else {
            btnAgree.setSelected(true);
            btnRegSubmit.setActivated(true);
        }
    }

    /*
     * 普通注册提交
     */
    public void btnRegSubmitClick(View v) {
        if (!btnRegSubmit.isActivated()) {
            return;
        }

        String username = editUserName.getText().toString();
        String password = editPassword.getText().toString();
        String password_confirm = editPasswordConfirm.getText().toString();
        String email = editEmail.getText().toString();
        if (username.equals("") || username == null) {
            Toast.makeText(RegisteredActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.length() < 6 || username.length() > 20) {
            T.showShort(RegisteredActivity.this, "用户名为6-20个字符");
            return;
        }

        if (StringUtils.isNumeric(username)) {
            T.showShort(RegisteredActivity.this, "用户名不能为纯数字");
            return;
        }

        if (password.equals("") || password == null) {
            Toast.makeText(RegisteredActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6 || password.length() > 20) {
            T.showShort(RegisteredActivity.this, "请输入6-20位密码");
            return;
        }

        if (password_confirm.equals("") || password_confirm == null) {
            Toast.makeText(RegisteredActivity.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password_confirm)) {
            Toast.makeText(RegisteredActivity.this, "两次密码不同", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.equals("") || email == null) {
            Toast.makeText(RegisteredActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SendData(username, password, password_confirm, email);
    }

    /**
     * 注册用户
     */
    public void SendData(String username, String password, String password_confirm, String email) {
        String url = myApplication.getpath()+Constants.URL_REGISTER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        params.put("password_confirm", password_confirm);
        params.put("email", email);
        params.put("client", "android");
        RemoteDataHandler.asyncLoginPostDataString(url, params, myApplication, new Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (data.getCode() == HttpStatus.SC_OK) {

                    Login login = Login.newInstanceList(json);
                    myApplication.setUserName(login.getUsername());
                    myApplication.setLoginKey(login.getKey());
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisteredActivity.this);
                                    builder.setTitle("选择");
                                    builder.setMessage("恭喜您获得"+redpacket_templateInfo.getRpacket_t_price()+"元红包");
                                    builder.setCancelable(false);
                                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(RegisteredActivity.this, RedpacketListActivity.class));
                                        }
                                    });
                                    builder.create().show();
                                }else{
                                    finish();
                                }
                                break;
                            case 2:
                                //优惠券
                                if (isgiving_obj.optBoolean("msg")){
                                    String info = isgiving_obj.optString("info");
                                    DebugUtils.printLogD(info);
                                    Voucher_templateInfo voucher_templateInfo = gson.fromJson(info, Voucher_templateInfo.class);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisteredActivity.this);
                                    builder.setTitle("选择");
                                    builder.setMessage("恭喜您获得"+voucher_templateInfo.getVoucher_price()+"元代金券");
                                    builder.setCancelable(false);
                                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(RegisteredActivity.this, VoucherListActivity.class));
                                        }
                                    });
                                    builder.create().show();
                                }else{
                                    finish();
                                }
                                break;
                            default:
                                finish();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShopHelper.showApiError(RegisteredActivity.this, json);
                }
            }
        });
    }

    /**
     * 用户注册协议
     */
    public void btnMemberDocumentClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.WAP_MEMBER_DOCUMENT));
        startActivity(intent);
    }
}
