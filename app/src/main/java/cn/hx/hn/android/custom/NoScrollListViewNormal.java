package cn.hx.hn.android.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 *
 *  @author fucekun
 */
public class NoScrollListViewNormal extends ListView {
	
	public NoScrollListViewNormal(Context context) {
		super(context);

	}

	public NoScrollListViewNormal(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
