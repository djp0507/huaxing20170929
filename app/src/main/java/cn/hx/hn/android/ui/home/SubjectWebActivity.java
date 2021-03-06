package cn.hx.hn.android.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import cn.hx.hn.android.R;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyExceptionHandler;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.custom.RoundProgressBar;
import cn.hx.hn.android.ui.type.GoodsDetailsActivity;
import cn.hx.hn.android.ui.type.GoodsListFragmentManager;

/**
 * 专题页面
 * @author KingKong-HE
 * @Time 2015-1-26
 * @Email KingKong@QQ.COM
 */
public class SubjectWebActivity extends Activity implements OnClickListener{
	private RoundProgressBar  roundProgressBar;
	private WebView webviewID;
	private String data;
	private MyShopApplication myApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject_view);
		MyExceptionHandler.getInstance().setContext(this);
		myApplication = (MyShopApplication) getApplication();
		data = getIntent().getStringExtra("data");
		
		webviewID = (WebView) findViewById(R.id.webviewID);
		roundProgressBar = (RoundProgressBar)findViewById(R.id.roundProgressBarID);
		ImageView imageBack = (ImageView) findViewById(R.id.imageBack);
		
		WebSettings settings = webviewID.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);//根据屏幕缩放
		settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);//根据屏幕缩放
		settings.setSupportZoom(false);
		settings.setBuiltInZoomControls(false);
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setAppCacheEnabled(false);
		webviewID.setWebChromeClient(new MyWebChromeClient()); 
		
		webviewID.loadUrl(data);
		
		webviewID.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				view.loadUrl("file:///android_asset/error.html");
			}
		});
		
		webviewID.addJavascriptInterface(new Object(){ 
            //这里我定义了一个拨打的方法   
			@JavascriptInterface
            public void mb_special_item_click(String type,String data){ 
            	if(type.equals("keyword")){//搜索关键字
					Intent intent = new Intent(SubjectWebActivity.this,GoodsListFragmentManager.class);
					intent.putExtra("keyword", data);
					intent.putExtra("gc_name", data);
					SubjectWebActivity.this.startActivity(intent);
				}else if(type.equals("special")){//专题编号
					webviewID.loadUrl(myApplication.getpath()+Constants.URL_SPECIAL+"&special_id="+data+"&type=html");
				}else if(type.equals("goods")){//商品编号
					Intent intent =new Intent(SubjectWebActivity.this,GoodsDetailsActivity.class);
					intent.putExtra("goods_id", data);
					SubjectWebActivity.this.startActivity(intent);
				}else if(type.equals("url")){//地址
					webviewID.loadUrl(data);
				}
            } 
        }, "android"); 
		
		imageBack.setOnClickListener(this);
		
	}
	
	private class MyWebChromeClient extends WebChromeClient {  
	    @Override  
	    public void onProgressChanged(WebView view, int newProgress) {  
	    	roundProgressBar.setProgress(newProgress);  
	        if(newProgress==100){  
	        	roundProgressBar.setVisibility(View.GONE);  
	        }  
	        super.onProgressChanged(view, newProgress);  
	    }  
	      
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageBack:
			
			finish();
			
			break;
		default:
			break;
		}
	}  
}
