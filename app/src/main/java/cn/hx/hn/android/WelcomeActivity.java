package cn.hx.hn.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.ShopHelper;
import cn.hx.hn.android.common.StringUtils;
import cn.hx.hn.android.common.T;
import cn.hx.hn.android.ui.fenxiao.ZhiBoActivity;

/**
 * 软件启动界面
 * @author KingKong-HE
 * @Time 2014-12-30
 * @Email KingKong@QQ.COM
 */
public class WelcomeActivity extends Activity {

	private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_view);
		MyExceptionHandler.getInstance().setContext(this);
		getPersimmions();
//		//每次进入页面就开始定位
//		LocationUtils locationUtils = new LocationUtils();
//		locationUtils.startLocation(getApplication(), new LocationUtils.LocationListener() {
//			@Override
//			public void succeed(String city, String longitude, String latitude) {
//				DebugUtils.printLogD("------", "city=" + city + "---longitude=" + longitude + "---latitude=" + latitude);
//				SpUtils.writeSp(getApplication(), SaveSp.MY_CITY, city);
//				SpUtils.writeSp(getApplication(), SaveSp.LONGITUDE, longitude);
//				SpUtils.writeSp(getApplication(), SaveSp.LATITUDE, latitude);
//			}
//			@Override
//			public void error(int type) {
//				DebugUtils.printLogD("------", "定位失败!");
//				SpUtils.writeSp(getApplication(), SaveSp.MY_CITY, "全国");
//				SpUtils.writeSp(getApplication(), SaveSp.LONGITUDE, "0");
//				SpUtils.writeSp(getApplication(), SaveSp.LATITUDE, "0");
//			}
//		});
    }

	@Override
	protected void onStart() {
		super.onStart();
		onLoad();
		if(i>=2){
			finish();
		}
	}

	private void onLoad() {
		Intent intent = getIntent();
		String scheme = intent.getScheme();
		Uri uri = intent.getData();
		System.out.println("scheme:"+scheme);
		if (uri != null) {
			String host = uri.getHost();
			String dataString = intent.getDataString();
			String id = uri.getQueryParameter("id");
			String url = uri.getQueryParameter("url");
			String path = uri.getPath();
			String path1 = uri.getEncodedPath();
			String queryString = uri.getQuery();
			System.out.println("host:"+host);
			System.out.println("dataString:"+dataString);
			System.out.println("id:"+id);
			System.out.println("url:"+url);
			System.out.println("path:"+path);
			System.out.println("path1:"+path1);
			System.out.println("queryString:"+queryString);
			if(!StringUtils.isEmpty(id)){
				if (ShopHelper.isLogin(WelcomeActivity.this, MyShopApplication.getInstance().getLoginKey())) {
					Intent intent1 = new Intent(getApplicationContext(), ZhiBoActivity.class);
					intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent1.putExtra("live_id", id);
					intent1.putExtra("live_url", url);
					startActivity(intent1);
					finish();
				}else {
//					finish();
					i++;
					T.showShort(getApplicationContext(),"请登录查看");
				}

			}else {
				Welcome();
			}
		}else {
			Welcome();
		}
	}

	private void Welcome() {
		//加入定时器 睡眠 2000毫秒 自动跳转页面
		new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent it=new Intent();
//				it.setClass(WelcomeActivity.this,StartActivity.class);
                it.setClass(WelcomeActivity.this,MainFragmentManager.class);
                startActivity(it);
                WelcomeActivity.this.finish();
            }
        }, 1000);
	}

	private String permissionInfo;
	private final int SDK_PERMISSION_REQUEST = 127;
	@TargetApi(23)
	private void getPersimmions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ArrayList<String> permissions = new ArrayList<String>();
			/***
			 * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
			 */
			// 定位精确位置
			if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
				permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
			}
			if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
				permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
			}
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
			// 读写权限
			if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
			}
			// 读取电话状态权限
			if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
				permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
			}

			if (permissions.size() > 0) {
				requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
			}
		}
	}

	@TargetApi(23)
	private boolean addPermission(ArrayList<String> permissionsList, String permission) {
		if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
			if (shouldShowRequestPermissionRationale(permission)){
				return true;
			}else{
				permissionsList.add(permission);
				return false;
			}

		}else{
			return true;
		}
	}

	@TargetApi(23)
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

	}

}
