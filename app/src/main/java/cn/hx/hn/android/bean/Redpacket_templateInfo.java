package cn.hx.hn.android.bean;

/**
 * Created by Administrator on 2016/12/2.
 */

public class Redpacket_templateInfo {
    private String rpacket_t_customimg_url;
    private String rpacket_t_price;

    @Override
    public String toString() {
        return "Redpacket_templateInfo{" +
                "rpacket_t_customimg_url='" + rpacket_t_customimg_url + '\'' +
                ", rpacket_t_price='" + rpacket_t_price + '\'' +
                '}';
    }

    public String getRpacket_t_customimg_url() {
        return rpacket_t_customimg_url;
    }

    public void setRpacket_t_customimg_url(String rpacket_t_customimg_url) {
        this.rpacket_t_customimg_url = rpacket_t_customimg_url;
    }

    public String getRpacket_t_price() {
        return rpacket_t_price;
    }

    public void setRpacket_t_price(String rpacket_t_price) {
        this.rpacket_t_price = rpacket_t_price;
    }
}
