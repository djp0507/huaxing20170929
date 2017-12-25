package cn.hx.hn.android.ui.mine;

import android.os.Bundle;

import cn.hx.hn.android.BaseActivity;
import cn.hx.hn.android.R;
import cn.hx.hn.android.common.MyExceptionHandler;

/**
 * 关于我们页面
 * @author KingKong-HE
 * @Time 2015-2-1
 * @Email KingKong@QQ.COM
 */
public class AboutActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_about);
		MyExceptionHandler.getInstance().setContext(this);
		setCommonHeader("关于我们");
	}
}
