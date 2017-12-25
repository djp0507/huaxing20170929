package cn.hx.hn.android.ui.fenxiao;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hx.hn.android.BaseActivity;
import cn.hx.hn.android.R;
import cn.hx.hn.android.bean.Mine;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.ShopHelper;
import cn.hx.hn.android.common.StringUtils;
import cn.hx.hn.android.common.T;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.ResponseData;
import cn.hx.hn.android.ui.mine.BindMobileActivity;
import cn.hx.hn.android.utils.ScreenUtil;

/**
 * Created by snm on 2016/9/23.
 */
public class FenxiaoAllActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout rlPointLog;
    private Dialog seletorDialog;
    private MyShopApplication myApplication;
    private TextView tv_isbinding;
    private TextView tv_bindingCard;
    private TextView tv_bindingAlipay;
    private TextView tv_cancel;
    private String name;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenxiao_all);
        setCommonHeader("分销中心");
        rlPointLog = (RelativeLayout) findViewById(R.id.rlPointLog);
        tv_isbinding = (TextView) findViewById(R.id.tv_binding_or_unbinding);
        myApplication = MyShopApplication.getInstance();
        initDialog();
        initDialogUI();
        isBinding();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBinding();
    }

    /**
     * 判断该账户是否有绑定支付宝或银行卡
     */
    private void isBinding() {
        String url = Constants.URL_ISBINDING_ALIPAY_OR_CARD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", myApplication.getLoginKey());
        RemoteDataHandler.asyncPostDataString(url, params, new RemoteDataHandler.Callback() {

            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (data.getCode() == HttpStatus.SC_OK) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        String is_bind_phone = obj.getString("is_bind_phone");
                        String is_bind_bank = obj.getString("is_bind_bank");


                        if (is_bind_phone.equals("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FenxiaoAllActivity.this);
                            builder.setTitle(R.string.hint)
                                    .setMessage(R.string.please_binding_phone)
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(new Intent(FenxiaoAllActivity.this, BindMobileActivity.class));
                                        }
                                    })
                                    .show();
                        } else if (is_bind_phone.equals("1")) {
                            if (is_bind_bank.equals("0")) {
                                tv_isbinding.setText(R.string.Binding_withdrawal);
                                tv_bindingAlipay.setVisibility(View.VISIBLE);
                                tv_bindingCard.setVisibility(View.VISIBLE);
                                tv_bindingCard.setText(R.string.binding_bank);
                                tv_bindingAlipay.setText(R.string.binding_alipay);
                                tv_isbinding.setTag(1);
                            } else if (is_bind_bank.equals("1")) {
                                JSONObject object = obj.getJSONObject("bind");
                                name = object.getString("bill_user_name");
                                number = object.getString("bill_type_number");
                                String type_code = object.getString("bill_type_code");
                                tv_isbinding.setTag(2);
                                if (type_code.equals("1")) {
                                    tv_isbinding.setText(R.string.unbinding_alipay);
                                    tv_bindingAlipay.setVisibility(View.VISIBLE);
                                    tv_bindingCard.setVisibility(View.GONE);
                                    tv_bindingAlipay.setText(R.string.unbinding_alipay);
                                } else if (type_code.equals("2")) {
                                    tv_isbinding.setText(R.string.unbinding_bank);
                                    tv_bindingAlipay.setVisibility(View.GONE);
                                    tv_bindingCard.setVisibility(View.VISIBLE);
                                    tv_bindingCard.setText(R.string.unbinding_bank);

                                }
                                tv_isbinding.setText(R.string.unBinding_withdrawal);
                            }
                        }

                    } catch (JSONException e) {
                        Toast.makeText(FenxiaoAllActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    private void initDialog() {
        if (seletorDialog == null) {
            seletorDialog = new Dialog(this, R.style.CommonDialog);
            seletorDialog.setCancelable(false);
            seletorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            seletorDialog.setContentView(R.layout.dialog_binding);
            seletorDialog.setCanceledOnTouchOutside(false);
            Window window = seletorDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = ScreenUtil.getInstance(this).getScreenWidth();
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initDialogUI() {
        tv_bindingCard = (TextView) seletorDialog.findViewById(R.id.tv_bindingcard);
        tv_bindingAlipay = (TextView) seletorDialog.findViewById(R.id.tv_bindingAlipay);
        tv_cancel = (TextView) seletorDialog.findViewById(R.id.tv_cancel);
        tv_bindingAlipay.setOnClickListener(this);
        tv_bindingCard.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int tag = (int) tv_isbinding.getTag();
        switch (v.getId()) {


            case R.id.tv_bindingcard:
                seletorDialog.dismiss();
                Intent intent = new Intent(FenxiaoAllActivity.this, BindingCardActivity.class);

                if (tag==1) {
                    intent.putExtra("type", 0);  //绑定银行卡
                } else{
                    intent.putExtra("type", 1); //解绑银行卡
                    intent.putExtra("name", name);
                    intent.putExtra("number", number);
                }
                startActivity(intent);
                break;
            case R.id.tv_bindingAlipay:

                seletorDialog.dismiss();
                Intent intent1 = new Intent(FenxiaoAllActivity.this, BindingAlipayActivity.class);
                if (tag==1) {
                    intent1.putExtra("type", 0); //绑定支付宝

                } else {
                    intent1.putExtra("type", 1); //解绑支付宝
                    intent1.putExtra("name", name);
                    intent1.putExtra("number", number);
                }
                startActivity(intent1);
                break;
            case R.id.tv_cancel:
                seletorDialog.dismiss();
                break;

        }
    }

    public void relatGoodsClick(View view) {
        startActivity(new Intent(getApplicationContext(), FenxiaoGoodsActivity.class));
    }

    public void relatTijiaoClick(View view) {
        startActivity(new Intent(getApplicationContext(), FenxiaoSettlementActivity.class));
    }

    public void relatOrderClick(View view) {
        startActivity(new Intent(getApplicationContext(), FenxiaoOrderActivity.class));
    }

    public void relattxClick(View view) {
        startActivity(new Intent(getApplicationContext(), FenxiaoTixianActivity.class));
    }

    public void relatBindingwithdrawalClick(View view) {
        FenxiaoAllActivity.this.setFinishOnTouchOutside(false);
        seletorDialog.show();
    }

    public void relatLiveClick(View view) {
        if (!StringUtils.isEmpty(movie_msg)) {
            T.showShort(getApplicationContext(), movie_msg);
        } else {
            ApplyVerifyMovie();
        }
    }

    public void ApplyVerifyMovie() {
        String url = Constants.URL_VERIFY_MOVIE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", MyShopApplication.getInstance().getLoginKey());

//        OkHttpUtil.postAsyn(getActivity(),url,new OkHttpUtil.ResultCallback<String>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                Logger.d(e.toString());
//                T.showShort(getActivity(),e.getMessage());
//            }
//
//            @Override
//            public void onResponse(String response) {
//                Logger.d(response);
//                try {
//                    JSONObject objError = new JSONObject(response);
//                    String error = objError.getString("error");
//                    if (error != null) {
//                        T.showShort(getActivity(),error);
//                        return;
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        },params);

        RemoteDataHandler.asyncLoginPostDataString(url, params, MyShopApplication.getInstance(), new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {

                String json = data.getJson();
                Logger.d(data.toString());
                if (data.getCode() == HttpStatus.SC_OK) {
                    /*审核通过了*/
                    if ("1".equals(json)) {
                    /*从没有申请过*/
                        T.showShort(getApplicationContext(), "请申请直播");
                        startActivity(new Intent(getApplicationContext(), ApplyLiveActivity.class));
                    } else {
                        T.showShort(getApplicationContext(), json);
//                        LiveCameraActivity.startActivity(getApplicationContext(),"rtmp://video-center.alivecdn.com/shopnc/test1?vhost=live.shopnctest.com", AlivcMediaFormat.OUTPUT_RESOLUTION_360P,false,AlivcMediaFormat.CAMERA_FACING_FRONT);
                        startActivity(new Intent(getApplicationContext(), BeginLiveActivity.class));
                    }
                } else {
                    /*失败有两种 正在进行中，没有通过*/
                    if (data.getCode() == 400) {
                        try {
                            JSONObject objError = new JSONObject(json);
                            String error = objError.getString("error");

                            if (!objError.isNull("true_name")) {
                                String true_name = objError.getString("true_name");
                                String card_number = objError.getString("card_number");
                                String card_before_image = objError.getString("card_before_image");
                                String card_behind_image = objError.getString("card_behind_image");
                                String card_before_image_url = objError.getString("card_before_image_url");
                                String card_behind_image_url = objError.getString("card_behind_image_url");
                                String is_agree = objError.getString("is_agree");
                                String member_id = objError.getString("member_id");
                                String movie_id = objError.getString("movie_id");

                                Intent intent = new Intent(getApplicationContext(), ApplyLiveActivity.class);
                                intent.putExtra("true_name", true_name);
                                intent.putExtra("card_number", card_number);
                                intent.putExtra("card_before_image", card_before_image);
                                intent.putExtra("card_behind_image", card_behind_image);
                                intent.putExtra("card_before_image_url", card_before_image_url);
                                intent.putExtra("card_behind_image_url", card_behind_image_url);
                                intent.putExtra("is_agree", is_agree);
                                intent.putExtra("member_id", member_id);
                                intent.putExtra("movie_id", movie_id);

                                startActivity(intent);
                            } else {
                                T.showShort(getApplicationContext(), error);
                            }
                        } catch (Exception e) {
                            T.showShort(getApplicationContext(), "获取失败");
                            e.printStackTrace();
                        }
                    } else {
                        ShopHelper.showApiError(getApplicationContext(), json);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMemberInfo();
    }

    String movie_msg = "";

    /**
     * 初始化加载我的信息
     */
    public void loadMemberInfo() {
        String url = ((MyShopApplication) getApplication()).getpath() + Constants.URL_MYSTOIRE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", MyShopApplication.getInstance().getLoginKey());

        RemoteDataHandler.asyncLoginPostDataString(url, params, MyShopApplication.getInstance(), new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {

                String json = data.getJson();
                Logger.d(json);
                if (data.getCode() == HttpStatus.SC_OK) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        String objJson = obj.getString("member_info");
                        Mine bean = Mine.newInstanceList(objJson);

                        if (bean != null) {

                            if (bean.getIs_movie() != null) {
                                if ("1".equals(bean.getIs_movie())) {
                                    rlPointLog.setVisibility(View.VISIBLE);
                                } else {
                                    rlPointLog.setVisibility(View.GONE);
                                }

                            }
                            movie_msg = bean.getIs_movie_msg();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ShopHelper.showApiError(getApplicationContext(), json);
                }
            }
        });
    }


}
