package cn.hx.hn.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import cn.hx.hn.android.R;
import cn.hx.hn.android.bean.Home3Data;
import cn.hx.hn.android.common.AnimateFirstDisplayListener;
import cn.hx.hn.android.common.Constants;
import cn.hx.hn.android.common.MyShopApplication;
import cn.hx.hn.android.common.SystemHelper;
import cn.hx.hn.android.ui.home.SubjectWebActivity;
import cn.hx.hn.android.ui.type.GoodsDetailsActivity;
import cn.hx.hn.android.ui.type.GoodsListFragmentManager;

/**
 * Created by Administrator on 2017/8/31.
 */

public class Home3RvAdapter extends BaseQuickAdapter<Home3Data,BaseViewHolder> {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = SystemHelper.getDisplayImageOptions();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private Context context;
    public Home3RvAdapter(ArrayList<Home3Data> data, Context context) {
        super(R.layout.tab_home_item_home3_gridview_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Home3Data home3Data) {
        ImageView ImageViewImagePic01 = baseViewHolder.getView(R.id.ImageViewImagePic01);
        imageLoader.displayImage(home3Data.getImage(), ImageViewImagePic01, options, animateFirstListener);
        OnImageViewClick(ImageViewImagePic01, home3Data.getType(),home3Data.getData());
    }

    public void OnImageViewClick(View view,final String type,final String data){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("keyword")){//搜索关键字
                    Intent intent = new Intent(context,GoodsListFragmentManager.class);
                    intent.putExtra("keyword", data);
                    intent.putExtra("gc_name", data);
                    context.startActivity(intent);
                }else if(type.equals("special")){//专题编号
                    Intent intent = new Intent(context,SubjectWebActivity.class);
                    intent.putExtra("data", ((MyShopApplication)context.getApplicationContext()).getpath()+ Constants.URL_SPECIAL+"&special_id="+data+"&type=html");
                    context.startActivity(intent);
                }else if(type.equals("goods")){//商品编号
                    Intent intent =new Intent(context,GoodsDetailsActivity.class);
                    intent.putExtra("goods_id", data);
                    context.startActivity(intent);
                }else if(type.equals("url")){//地址
                    Intent intent = new Intent(context,SubjectWebActivity.class);
                    intent.putExtra("data", data);
                    context.startActivity(intent);
                }
            }
        });
    }
}
