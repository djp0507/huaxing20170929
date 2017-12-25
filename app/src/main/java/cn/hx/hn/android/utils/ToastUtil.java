package cn.hx.hn.android.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import cn.hx.hn.android.R;


/**
 * 一些dialog的封装工具类
 * @author zx
 *
 */
public class ToastUtil {
	private static Toast toast;
	private static Dialog cancel_dialog;
	private static TextView name_dialog;
	public static void showToast(Context c, String info){
		if (toast != null) {
			toast.cancel();
		}
		if (c == null){
			return;
		}

		toast.makeText(c, info, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Loding加载动画（DIY内容）
	 */
	public static void showLodingDialog(Context context, String text){
		if (context == null){
			return;
		}
		if (toast != null) {
			toast.cancel();
		}
		if (cancel_dialog != null){
			cancel_dialog.dismiss();
		}
		cancel_dialog =  new Dialog(context, R.style.progressBar_LodingDialog);

		Window win = cancel_dialog.getWindow();
		WindowManager.LayoutParams params = new  WindowManager.LayoutParams(Gravity.CENTER);
//		params.dimAmount=0.0f;
		win.setAttributes(params);
		cancel_dialog.setContentView(R.layout.my_loding_dialog);
//		name_dialog=(TextView) cancel_dialog.findViewById(R.id.textView_loding_dialog);
//		name_dialog.setText(text);
		cancel_dialog.setCanceledOnTouchOutside(false); //为true点击对话框以外时候ProgressDialog就会关闭反之不关
		cancel_dialog.show();
	}
	
	/**
	 * Dialog消失
	 */
	public static  void dissMissDialog(){
		try {
			if(cancel_dialog!=null && cancel_dialog.isShowing()){
				cancel_dialog.cancel();
				cancel_dialog=null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public  static void showSystemToast(Context c, String info){
		if (c == null){
			return;
		}
		if (toast != null) {
			toast.cancel();
		}
		else{

		}
		toast = new Toast(c);
		View layout = LayoutInflater.from(c).inflate(R.layout.my_toast, null);
//		ImageView image = (ImageView) layout.findViewById(R.id.toast_icon1);
//		//设置布局中图片视图中图片
//		image.setImageResource(R.mipmap.ic_launcher);
		TextView text = (TextView) layout.findViewById(R.id.toast_tv);
		toast.setGravity(Gravity.CENTER , 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		//设置内容
		text.setText(info);
		toast.show();
	}

	private static  Snackbar snackbar;
	public  static void showSnackbar(View view, String info){
		if (snackbar != null){
			snackbar.dismiss();
		}
		snackbar = Snackbar.make(view, info, Snackbar.LENGTH_SHORT);
		Snackbar.SnackbarLayout snacklayout = (Snackbar.SnackbarLayout) snackbar.getView();//snackd布局
		TextView lefttv = (TextView) snacklayout.findViewById(R.id.snackbar_text);
		lefttv.setTextColor(Color.parseColor("#ffffff"));
		snackbar.show();
	}

}
