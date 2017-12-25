package cn.hx.hn.android.adapter;

import android.animation.Animator;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.hx.hn.android.R;
import cn.hx.hn.android.bean.Store_credit;
import cn.hx.hn.android.bean.Store_list;
import cn.hx.hn.android.common.LoadImage;

/**
 * Created by Administrator on 2016/12/9.
 */

public class Merchantsadapter extends BaseQuickAdapter<Store_list, BaseViewHolder> {
    public Merchantsadapter( List<Store_list> data) {
        super(R.layout.stop_item, data);
    }
    @Override
    protected void startAnim(Animator anim, int index) {
        super.startAnim(anim, index);
        if (index < 5)
            anim.setStartDelay(index * 150);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, Store_list store_list) {
        Store_credit store_credit = store_list.getStore_credit();
        baseViewHolder.setText(R.id.stop_store_name,store_list.getStore_name()).
                setText(R.id.stop_seller_name,store_list.getSeller_name()).
                setText(R.id.stop_store_zy,store_list.getStore_zy()).
                setText(R.id.stop_goods_count,store_list.getArea_info()).
                setText(R.id.stop_num_sales_jq,store_list.getDistance()).
                setText(R.id.stopstore_credit_average,store_list.getStore_credit_average()+"").
                setText(R.id.stopstore_credit_percent,store_list.getStore_credit_percent()+"").
                setText(R.id.stopstore_desccredit,store_list.getStore_credit().getStore_desccredit().getText() + store_list.getStore_credit().getStore_desccredit().getCredit()).
                setText(R.id.stopstore_deliverycredit,store_list.getStore_credit().getStore_deliverycredit().getText()+ store_list.getStore_credit().getStore_deliverycredit().getCredit()).
                setText(R.id.stopstore_servicecredit,store_list.getStore_credit().getStore_servicecredit().getText()+store_list.getStore_credit().getStore_servicecredit().getCredit());
        LoadImage.loadImg1(mContext, (ImageView) baseViewHolder.getView(R.id.store_logo),store_list.getStore_logo());
    }
}
