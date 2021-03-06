package cn.hx.hn.android;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.readystatesoftware.viewbadger.BadgeView;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.SaveSp;
import cn.hx.hn.android.http.RemoteDataHandler;
import cn.hx.hn.android.http.RemoteDataHandler.Callback;
import cn.hx.hn.android.http.ResponseData;
import cn.hx.hn.android.ui.cart.CartFragment;
import cn.hx.hn.android.ui.home.HomeFragment;
import cn.hx.hn.android.ui.mine.MineFragment;
import cn.hx.hn.android.ui.store.MerchantsFragment;
import cn.hx.hn.android.ui.type.OneTypeFragment;
import cn.hx.hn.android.utils.SpUtils;
import customview.ConfirmDialog;
import util.DownloadAppUtils;

;

/**
 * 底部菜单管理界面
 *
 * @author KingKong-HE
 * @Time 2014-12-30
 * @Email KingKong@QQ.COM
 */
public class MainFragmentManager extends FragmentActivity {

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    /**
     * 定义首页、分类、购物车、我的Fragment
     */
    private HomeFragment homeFragment;
    private OneTypeFragment typeFragment;
    private MineFragment mineFragment;
    private CartFragment cartFragment;
    private MerchantsFragment merchantsFragment;

    /**
     * 定义首页、分类、购物车、我的tab的图标
     */
    private RadioButton btnHomeID;
    private RadioButton btnClassID;
    private RadioButton btnCartID;
    private RadioButton btnMineID;
    private RadioButton btnStopID;


    private Button btn3;
    private BadgeView badge;

    private MyShopApplication myApplication;

    /**
     * 对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        MyExceptionHandler.getInstance().setContext(this);
        fragmentManager = getSupportFragmentManager();

        myApplication = (MyShopApplication) getApplicationContext();

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBoradcastReceiver();
        myApplication.getmSocket().connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        myApplication.getmSocket().disconnect();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.SHOW_CART_URL)) {
                CartIn();//显示购物车
                btnCartID.setChecked(true);
            } else if (action.equals(Constants.SHOW_HOME_URL)) {
                HomeIn();//显示首页
                btnHomeID.setChecked(true);
            } else if (action.equals(Constants.SHOW_Classify_URL)) {
                TupeIn();//显示分类页面
                btnClassID.setChecked(true);
            } else if (action.equals(Constants.SHOW_Mine_URL)) {
                MineIn();//显示我的商城页面
                btnMineID.setChecked(true);
            } else if (action.equals(Constants.SHOW_CART_NUM)) {
                setCartNumShow();
            }
        }
    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.SHOW_CART_URL);
        myIntentFilter.addAction(Constants.SHOW_HOME_URL);
        myIntentFilter.addAction(Constants.SHOW_Classify_URL);
        myIntentFilter.addAction(Constants.SHOW_Mine_URL);
        myIntentFilter.addAction(Constants.SHOW_CART_NUM);
        registerReceiver(mBroadcastReceiver, myIntentFilter); //注册广播       
    }


    /**
     * 隐藏所有的fragment
     *
     * @param transaction 用于对fragment进行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (typeFragment != null) {
            transaction.hide(typeFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
        if (cartFragment != null) {
            transaction.hide(cartFragment);
        }
        if (merchantsFragment != null) {
            transaction.hide(merchantsFragment);
        }
    }

    /**
     * 初始化界面，并设置3个tab的监听
     */
    private void initViews() {
        // //////////////////// find View ////////////////////////////
        btnHomeID = (RadioButton) this.findViewById(R.id.btnHomeID);
        btnClassID = (RadioButton) this.findViewById(R.id.btnClassID);
        btnCartID = (RadioButton) this.findViewById(R.id.btnCartID);
        btnMineID = (RadioButton) this.findViewById(R.id.btnMineID);
        btnStopID = (RadioButton) this.findViewById(R.id.btnStopID);

        btn3 = (Button) this.findViewById(R.id.btn3);
        badge = new BadgeView(this, btn3);
        badge.setTextSize(10);
        badge.setVisibility(View.GONE);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        if (!(myApplication.getLoginKey().equals("null") || myApplication.getLoginKey().equals(""))) {
            setCartNumShow();
        }

        MyRadioButtonClickListener listener = new MyRadioButtonClickListener();
        btnHomeID.setOnClickListener(listener);
        btnClassID.setOnClickListener(listener);
        btnCartID.setOnClickListener(listener);
        btnMineID.setOnClickListener(listener);
        btnStopID.setOnClickListener(listener);

        //首次进入显示首页界面
        HomeIn();

        //判断是否有新版本
        versionUpdate();

    }

    public void setCartNumShow() {
        String url = myApplication.getpath() + Constants.URL_GET_CART_NUM;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", myApplication.getLoginKey());
        RemoteDataHandler.asyncPostDataString(url, params, new Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                String json = data.getJson();
                if (data.getCode() == HttpStatus.SC_OK) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        String num = obj.getString("cart_count");
//                        Log.i("QINM",num);
                        badge.setText(num);
                        badge.show();
                    } catch (JSONException e) {
                        Toast.makeText(MainFragmentManager.this, "获取购物车数量失败", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    /**
     * 设置开启的tab首页页面
     */
    public void HomeIn() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            transaction.add(R.id.content, homeFragment);
        } else {
            transaction.show(homeFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 设置开启的tab分类页面
     */
    public void TupeIn() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        if (typeFragment == null) {
            typeFragment = new OneTypeFragment();
            transaction.add(R.id.content, typeFragment);
        } else {
            transaction.show(typeFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 设置开启的tab我的页面
     */
    public void MineIn() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        if (mineFragment == null) {
            mineFragment = new MineFragment();
            transaction.add(R.id.content, mineFragment);
        } else {
            mineFragment.setLoginInfo();
            transaction.show(mineFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 设置开启的tab购物车页面
     */
    public void CartIn() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        if (cartFragment == null) {
            cartFragment = new CartFragment();
            transaction.add(R.id.content, cartFragment);
        } else {
            transaction.show(cartFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 设置开启的店铺页面
     */
    public void MerchantsIn() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        if (merchantsFragment == null) {
            merchantsFragment = new MerchantsFragment();
            transaction.add(R.id.content, merchantsFragment);
        } else {
            transaction.show(merchantsFragment);
        }
        transaction.commitAllowingStateLoss();
    }


    private String downloadUrl = "";
    private String versionName = "";

    /**
     * 获取是否有最新版本
     */
    public void versionUpdate() {
        RemoteDataHandler.asyncDataStringGet(myApplication.getpath() + Constants.URL_VERSION_UPDATE, new Callback() {
            @Override
            public void dataLoaded(ResponseData data) {
                if (data.getCode() == HttpStatus.SC_OK) {
                    String json = data.getJson();
                    Log.e("abcdefg", json);
                    try {
                        JSONObject obj = new JSONObject(json);
                        versionName = obj.getString("version");
                        downloadUrl = obj.getString("url");
                        //先查询是否忽略此次更新
                        if (!TextUtils.isEmpty(SpUtils.getSpString(MainFragmentManager.this,SaveSp.IGNORE_ANDROID_V))){
                            if (SpUtils.getSpString(MainFragmentManager.this,SaveSp.IGNORE_ANDROID_V).equals(versionName)){
                                return;
                            }
                        }
                        if (!BuildConfig.VERSION_NAME.equals(versionName)) {
                            AlertDialog dialog = new AlertDialog.Builder(MainFragmentManager.this)//.setCancelable(false)
                                   .setTitle("提示").setMessage("您有新的版本需要更新.更新后才能体验更多新功能.")
                                    .setNegativeButton("更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (cn.hx.hn.android.utils.NetUtils.isWifi(MainFragmentManager.this)) {
                                                checkAndUpdate();
                                            } else {
                                                AlertDialog dialog1 = new AlertDialog.Builder(MainFragmentManager.this).setCancelable(false)
                                                        .setTitle("提示").setMessage("当前网络不是处于wifi状态下,是否更新?!")
                                                        .setNegativeButton("更新", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                checkAndUpdate();
                                                            }
                                                        })
                                                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface arg0, int arg1) {

                                                            }
                                                        }).create();
                                                dialog1.show();}
                                        }
                                    })
                                    .setPositiveButton("此版本不在提示更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            SpUtils.writeSp(MainFragmentManager.this, SaveSp.IGNORE_ANDROID_V, versionName);
                                        }
                                    }).create();
                            dialog.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//						Toast.makeText(MainFragmentManager.this, R.string.load_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAndUpdate() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            update();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                update();
            } else {//申请权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    //根据versionName判断跟新
    private void update() {
//        UpdateAppUtils.from(this)
//                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME)
//                .serverVersionName(versionName)
//                .serverVersionCode(BuildConfig.VERSION_CODE)
//                .apkPath(downloadUrl)
//                .downloadBy(UpdateAppUtils.DOWNLOAD_BY_BROWSER)
//                .isForce(true)
//                .update();
        DownloadAppUtils.downloadForAutoInstall(this, downloadUrl, "hx.apk", BuildConfig.app_name_android+versionName);
    }


    //权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    update();
                } else {
                    new ConfirmDialog(this, new feature.Callback() {
                        @Override
                        public void callback(int position) {
                            if (position == 1) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                startActivity(intent);
                            }
                        }
                    }).setContent("暂无读写SD卡权限\n是否前往设置？").show();
                }
                break;
        }
    }


    class MyRadioButtonClickListener implements View.OnClickListener {
        public void onClick(View v) {
            RadioButton btn = (RadioButton) v;
            switch (btn.getId()) {
                case R.id.btnHomeID:
                    HomeIn();
                    break;
                case R.id.btnClassID:
                    TupeIn();
                    break;
                case R.id.btnCartID:
                    CartIn();
                    break;
                case R.id.btnMineID:
                    MineIn();
                    break;
                case R.id.btnStopID:
                    MerchantsIn();
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
//            System.exit(0);
        }
    }

}
