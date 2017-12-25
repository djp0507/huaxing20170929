package cn.hx.hn.android.bean;

/**
 * Created by Administrator on 2016/12/2.
 */

public class Voucher_templateInfo {
    private String voucher_price;

    @Override
    public String toString() {
        return "Voucher_templateInfo{" +
                "voucher_price='" + voucher_price + '\'' +
                '}';
    }

    public String getVoucher_price() {
        return voucher_price;
    }

    public void setVoucher_price(String voucher_price) {
        this.voucher_price = voucher_price;
    }
}
