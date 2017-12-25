package cn.hx.hn.android.ui.mine;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.logger.Logger;
import com.ta.utdid2.android.utils.StringUtils;

import org.apache.http.HttpStatus;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hx.hn.android.R;
import cn.hx.hn.android.bean.Mine;
import cn.hx.hn.android.bean.event.RefreshEvent;
import cn.hx.hn.android.common.AnimateFirstDisplayListener;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.SaveSp;
import cn.hx.hn.android.common.ShopHelper;
import cn.hx.hn.android.common.SystemHelper;
import cn.hx.hn.android.common.T;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.ResponseData;
import cn.hx.hn.android.ui.fenxiao.ApplyLiveActivity;
import cn.hx.hn.android.ui.fenxiao.BeginLiveActivity;
import cn.hx.hn.android.ui.fenxiao.FenxiaoAllActivity;
import cn.hx.hn.android.ui.fenxiao.FenxiaoGoodsActivity;
import cn.hx.hn.android.ui.fenxiao.FenxiaoOrderActivity;
import cn.hx.hn.android.ui.fenxiao.FenxiaoSettlementActivity;
import cn.hx.hn.android.ui.fenxiao.FenxiaoTixianActivity;
import cn.hx.hn.android.ui.type.AddressListActivity;
import cn.hx.hn.android.ui.type.GoodsBrowseActivity;
import cn.hx.hn.android.utils.SpUtils;

/**
 * 我的
 *
 * @author dqw
 * @Time 2015-7-6
 */
public class MineFragment extends Fragment {

    private MyShopApplication myApplication;

    private LinearLayout llLogin;
    private LinearLayout llMemberInfo;
    private ImageView ivMemberAvatar;
    private TextView tvMemberName;
    private ImageView ivFavGoods;
    private TextView tvFavGoodsCount;
    private ImageView ivFavStore;
    private TextView tvFavStoreCount;
    private TextView tv_Member_v;
    private TextView daifukuan, daishouhuo, daiziti, daipingjia, tuikuang;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = SystemHelper.getRoundedBitmapDisplayImageOptions(100);
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    /*分销*/
    private LinearLayout fenxiao_llOrderNoeval, fenxiao_llOrderNotakes, fenxiao_llOrderSend, fenxiao_llOrderNew, ll_fenxiao;
    private Button fenxiao_btnOrderAll;

    private View tuijianma;
    private TextView my_tuijianma;

    private CheckBox switch_CheckBox;
    private View offline_show;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewLayout = inflater.inflate(R.layout.main_mine_view, container, false);
        MyExceptionHandler.getInstance().setContext(getActivity());
        myApplication = (MyShopApplication) getActivity().getApplicationContext();

        initSettingButton(viewLayout);
        initMemberButton(viewLayout);
        initOrderButton(viewLayout);
        initAssetButton(viewLayout);
        initFenxiaoLinearlayout(viewLayout);

        return viewLayout;
    }

    private void initFenxiaoLinearlayout(View viewLayout) {
        fenxiao_llOrderNoeval = (LinearLayout) viewLayout.findViewById(R.id.fenxiao_llOrderNoeval);
        fenxiao_llOrderNotakes = (LinearLayout) viewLayout.findViewById(R.id.fenxiao_llOrderNotakes);
        fenxiao_llOrderSend = (LinearLayout) viewLayout.findViewById(R.id.fenxiao_llOrderSend);
        fenxiao_llOrderNew = (LinearLayout) viewLayout.findViewById(R.id.fenxiao_llOrderNew);
        LinearLayout fenxiao_llOrdertixian = (LinearLayout) viewLayout.findViewById(R.id.fenxiao_llOrdertixian);
        fenxiao_btnOrderAll = (Button) viewLayout.findViewById(R.id.fenxiao_btnOrderAll);
        ll_fenxiao = (LinearLayout) viewLayout.findViewById(R.id.ll_fenxiao);
        /*直播*/
        fenxiao_llOrderNoeval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO
                if(StringUtils.isEmpty(movie_msg)){
                    ApplyVerifyMovie();
                }else {
                    T.showShort(getActivity(),movie_msg);
                }

//                   startActivity(new Intent(getActivity(), ApplyLiveActivity.class));
//                   startActivity(new Intent(getActivity(), PushActivity.class));
            }
        });
        /*全部*/
        fenxiao_btnOrderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ZhiBoActivity.class));
                startActivity(new Intent(getActivity(), FenxiaoAllActivity.class));

            }
        });
        /*分销结算*/
        fenxiao_llOrderNotakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FenxiaoSettlementActivity.class));
            }
        });
        /*分销订单*/
        fenxiao_llOrderSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FenxiaoOrderActivity.class));
            }
        });
        /*分销商品 */
        fenxiao_llOrderNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FenxiaoGoodsActivity.class));
            }
        });
        /*分销提现列表*/
        fenxiao_llOrdertixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FenxiaoTixianActivity.class));
            }
        });
    }

    /**
     * 初始化设置相关按钮
     *
     * @param viewLayout
     */
    private void initSettingButton(View viewLayout) {
        //推荐码
        tuijianma = viewLayout.findViewById(R.id.tuijianma);
        my_tuijianma = (TextView) viewLayout.findViewById(R.id.my_tuijianma);

        switch_CheckBox = (CheckBox) viewLayout.findViewById(R.id.switch_CheckBox);
        offline_show = viewLayout.findViewById(R.id.offline_show);
        switch_CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtils.writeSp(getActivity(),SaveSp.OFFLINE_SHOW,isChecked);
                EventBus.getDefault().post(new RefreshEvent());
            }
        });
        //设置
        Button btnSetting = (Button) viewLayout.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                }
            }
        });

        //设置2
        RelativeLayout rlSetting = (RelativeLayout) viewLayout.findViewById(R.id.rlSetting);
        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                }
            }
        });

        //IM
        Button btnIm = (Button) viewLayout.findViewById(R.id.btnIm);
        btnIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
//                    startActivity(new Intent(getActivity(), IMFriendsListActivity.class));
                    startActivity(new Intent(getActivity(), IMNewListActivity.class));
                }
            }
        });

        //登录
        llLogin = (LinearLayout) viewLayout.findViewById(R.id.llLogin);
        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        llMemberInfo = (LinearLayout) viewLayout.findViewById(R.id.llMemberInfo);
        ivMemberAvatar = (ImageView) viewLayout.findViewById(R.id.ivMemberAvatar);
        tvMemberName = (TextView) viewLayout.findViewById(R.id.tvMemberName);


        daifukuan = (TextView) viewLayout.findViewById(R.id.daifukuang_num);
        daishouhuo = (TextView) viewLayout.findViewById(R.id.daishouhuo_num);
        daiziti = (TextView) viewLayout.findViewById(R.id.daiziti_num);
        daipingjia = (TextView) viewLayout.findViewById(R.id.daipingjia_num);
        tuikuang = (TextView) viewLayout.findViewById(R.id.tuikuan_num);

    }

    /**
     * 初始化用户信息相关按钮
     *
     * @param viewLayout
     */
    private void initMemberButton(View viewLayout) {
        //商品收藏
        LinearLayout llFavGoods = (LinearLayout) viewLayout.findViewById(R.id.llFavGoods);
        llFavGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), FavGoodsListActivity.class));
                }
            }
        });
        ivFavGoods = (ImageView) viewLayout.findViewById(R.id.ivFavGoods);
        tvFavGoodsCount = (TextView) viewLayout.findViewById(R.id.tvFavGoodsCount);

        //收藏店铺
        LinearLayout llFavStore = (LinearLayout) viewLayout.findViewById(R.id.llFavStore);
        llFavStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), FavStoreListActivity.class));
                }
            }
        });
        ivFavStore = (ImageView) viewLayout.findViewById(R.id.ivFavStore);
        tvFavStoreCount = (TextView) viewLayout.findViewById(R.id.tvFavStoreCount);
        tv_Member_v = (TextView) viewLayout.findViewById(R.id.tv_Member_v);
        //我的足迹
        LinearLayout llZuji = (LinearLayout) viewLayout.findViewById(R.id.llZuji);
        llZuji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), GoodsBrowseActivity.class));
                }
            }
        });

        //收货地址
        RelativeLayout rlAddress = (RelativeLayout) viewLayout.findViewById(R.id.rlAddress);
        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    Intent intent = new Intent(getActivity(), AddressListActivity.class);
                    intent.putExtra("addressFlag", "0");
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化订单相关按钮
     *
     * @param viewLayout
     */
    private void initOrderButton(View viewLayout) {
        //全部订单
        Button btnOrderAll = (Button) viewLayout.findViewById(R.id.btnOrderAll);
        btnOrderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderList("");
            }
        });
        //待付款订单
        LinearLayout llOrderNew = (LinearLayout) viewLayout.findViewById(R.id.llOrderNew);
        setOrderButtonEvent(llOrderNew, "state_new");
        //待收货
        LinearLayout llOrderSend = (LinearLayout) viewLayout.findViewById(R.id.llOrderSend);
        setOrderButtonEvent(llOrderSend, "state_send");
        //待自提订单
        LinearLayout llOrderNotakes = (LinearLayout) viewLayout.findViewById(R.id.llOrderNotakes);
        setOrderButtonEvent(llOrderNotakes, "state_notakes");
        //待评价订单
        LinearLayout llOrderNoeval = (LinearLayout) viewLayout.findViewById(R.id.llOrderNoeval);
        setOrderButtonEvent(llOrderNoeval, "state_noeval");
        //退款退货
        LinearLayout llRefund = (LinearLayout) viewLayout.findViewById(R.id.llRefund);
        llRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderExchangeListActivity.class));
            }
        });
    }

    /**
     * 初始化财产相关按钮
     *
     * @param viewLayout
     */
    private void initAssetButton(View viewLayout) {
        //全部财产
        Button btnMyAsset = (Button) viewLayout.findViewById(R.id.btnMyAsset);
        btnMyAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), MyAssetActivity.class));
                }
            }
        });

        //预存款
        LinearLayout llPredeposit = (LinearLayout) viewLayout.findViewById(R.id.llPredeposit);
        llPredeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), PredepositActivity.class));
                }
            }
        });

        //充值卡
        LinearLayout llRechargeCard = (LinearLayout) viewLayout.findViewById(R.id.llRechargeCard);
        llRechargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), RechargeCardLogActivity.class));
                }
            }
        });

        //代金券
        LinearLayout llVoucherList = (LinearLayout) viewLayout.findViewById(R.id.llVoucherList);
        llVoucherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), VoucherListActivity.class));
                }
            }
        });

        //红包
        LinearLayout llRedpacket = (LinearLayout) viewLayout.findViewById(R.id.llRedpacketList);
        llRedpacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), RedpacketListActivity.class));
                }
            }
        });

        //积分
        LinearLayout llPointLog = (LinearLayout) viewLayout.findViewById(R.id.llPointLog);
        llPointLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
                    startActivity(new Intent(getActivity(), PointLogActivity.class));
                }
            }
        });
    }

    /**
     * 设置订单按钮事件
     */
    private void setOrderButtonEvent(LinearLayout btn, final String stateType) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderList(stateType);
            }
        });
    }

    /**
     * 显示订单列表
     */
    private void showOrderList(String stateType) {
        if (ShopHelper.isLogin(getActivity(), myApplication.getLoginKey())) {
            Intent intent = new Intent(getActivity(), OrderListActivity.class);
            intent.putExtra("state_type", stateType);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setLoginInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBoradcastReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.LOGIN_SUCCESS_URL)) {
                loadMemberInfo();
            }
        }
    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.LOGIN_SUCCESS_URL);
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);  //注册广播
    }

    /**
     * 检测是否登录
     */
    public void setLoginInfo() {
        String loginKey = myApplication.getLoginKey();
        if (loginKey != null && !loginKey.equals("")) {
            llMemberInfo.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
            ivFavGoods.setVisibility(View.GONE);
            ivFavStore.setVisibility(View.GONE);
            tvFavGoodsCount.setVisibility(View.VISIBLE);
            tvFavStoreCount.setVisibility(View.VISIBLE);
            loadMemberInfo();
        } else {
            llMemberInfo.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            ivFavGoods.setVisibility(View.VISIBLE);
            ivFavStore.setVisibility(View.VISIBLE);
            tvFavGoodsCount.setVisibility(View.GONE);
            tvFavStoreCount.setVisibility(View.GONE);
            tuijianma.setVisibility(View.GONE);
            offline_show.setVisibility(View.GONE);
        }
    }

    String movie_msg = "";

    /**
     * 初始化加载我的信息
     */
    public void loadMemberInfo() {
        String url = myApplication.getpath()+Constants.URL_MYSTOIRE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", myApplication.getLoginKey());

        RemoteDataHandler.asyncLoginPostDataString(url, params, myApplication, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
//                Logger.d(json);
                if (data.getCode() == HttpStatus.SC_OK) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        String objJson = obj.getString("member_info");
                        Mine bean = Mine.newInstanceList(objJson);

                        if (bean != null) {
                            tvMemberName.setText(bean.getMemberName() == null ? "" : bean.getMemberName());
                            imageLoader.displayImage(bean.getMemberAvatar(), ivMemberAvatar, options, animateFirstListener);
                            tvFavGoodsCount.setText(bean.getFavGoods() == null ? "0" : bean.getFavGoods());
                            tvFavStoreCount.setText(bean.getFavStore() == null ? "0" : bean.getFavStore());
                            tv_Member_v.setText(bean.getLevelName() == null ? "0" : bean.getLevelName());
                            if (bean.getOrderReturn().equals("0")) {
                                tuikuang.setVisibility(View.INVISIBLE);
                            } else {
                                tuikuang.setText(bean.getOrderReturn());
                                tuikuang.setVisibility(View.VISIBLE);
                            }
                            if (bean.getOrderNoeval().equals("0")) {
                                daipingjia.setVisibility(View.INVISIBLE);
                            } else {
                                daipingjia.setText(bean.getOrderNoeval());
                                daipingjia.setVisibility(View.VISIBLE);
                            }
                            if (bean.getOrderNotakes().equals("0")) {
                                daiziti.setVisibility(View.INVISIBLE);
                            } else {
                                daiziti.setText(bean.getOrderNotakes());
                                daiziti.setVisibility(View.VISIBLE);
                            }
                            if (bean.getOrderNoreceipt().equals("0")) {
                                daishouhuo.setVisibility(View.INVISIBLE);
                            } else {
                                daishouhuo.setText(bean.getOrderNoreceipt());
                                daishouhuo.setVisibility(View.VISIBLE);
                            }
                            if (bean.getOrderNopay().equals("0")) {
                                daifukuan.setVisibility(View.INVISIBLE);
                            } else {
                                daifukuan.setText(bean.getOrderNopay());
                                daifukuan.setVisibility(View.VISIBLE);
                            }
                            //d/d
                            if (bean.getIs_distr() != null) {
                                if ("1".equals(bean.getIs_distr())) {
                                    ll_fenxiao.setVisibility(View.VISIBLE);
                                } else {
                                    ll_fenxiao.setVisibility(View.GONE);
                                }
                            } else {
                                ll_fenxiao.setVisibility(View.GONE);
                            }
                            if (bean.getIs_movie() != null) {
                                if ("1".equals(bean.getIs_movie())) {
                                    fenxiao_llOrderNoeval.setVisibility(View.VISIBLE);
                                } else {
                                    fenxiao_llOrderNoeval.setVisibility(View.GONE);
                                }
                            }
                            movie_msg = bean.getIs_movie_msg();

                            if (TextUtils.isEmpty(bean.getRecommended_code())){
//                                tuijianma = viewLayout.findViewById(R.id.tuijianma);
//                                my_tuijianma = (TextView) viewLayout.findViewById(R.id.my_tuijianma);
                                SpUtils.writeSp(getActivity(), SaveSp.RECOMMENDED_CODE,"");
                                tuijianma.setVisibility(View.GONE);
                            }else{
                                tuijianma.setVisibility(View.VISIBLE);
                                SpUtils.writeSp(getActivity(), SaveSp.RECOMMENDED_CODE,bean.getRecommended_code());
                                my_tuijianma.setText("我的推荐码 : " + bean.getRecommended_code());
                            }
                            switch_CheckBox.setChecked(SpUtils.getSpBoolean(getActivity(),SaveSp.OFFLINE_SHOW));
                            if (bean.getOffline_show().equals("1")){
                                offline_show.setVisibility(View.VISIBLE);
                            }else{
                                offline_show.setVisibility(View.GONE);
                            }
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

    public void ApplyVerifyMovie() {
        String url = Constants.URL_VERIFY_MOVIE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", myApplication.getLoginKey());

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

        RemoteDataHandler.asyncLoginPostDataString(url, params, myApplication, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(ResponseData data) {

                String json = data.getJson();
                Logger.d(data.toString());
                if (data.getCode() == HttpStatus.SC_OK) {
                    /*审核通过了*/
                    if ("1".equals(json)) {
                    /*从没有申请过*/
                        T.showShort(getActivity(), "请申请直播");
                        startActivity(new Intent(getActivity(), ApplyLiveActivity.class));
                    } else {
//                        T.showShort(getActivity(),json);
//                        LiveCameraActivity.startActivity(getActivity(),"rtmp://video-center.alivecdn.com/shopnc/test1?vhost=live.shopnctest.com", AlivcMediaFormat.OUTPUT_RESOLUTION_360P,false,AlivcMediaFormat.CAMERA_FACING_FRONT);
                        startActivity(new Intent(getActivity(), BeginLiveActivity.class));
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

                                Intent intent = new Intent(getActivity(), ApplyLiveActivity.class);
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
                                T.showShort(getActivity(), error);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShopHelper.showApiError(getActivity(), json);
                    }
                }
            }
        });
    }
}