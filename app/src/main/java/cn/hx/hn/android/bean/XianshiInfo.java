package cn.hx.hn.android.bean;

/**
 * Created by Administrator on 2016/12/7.
 */

public class XianshiInfo {
    private long end_time;
    private String xianshi_price;
    private long start_time;
    private int max_limit;

    @Override
    public String toString() {
        return "XianshiInfo{" +
                "end_time=" + end_time +
                ", xianshi_price='" + xianshi_price + '\'' +
                ", start_time=" + start_time +
                ", max_limit=" + max_limit +
                '}';
    }

    public int getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(int max_limit) {
        this.max_limit = max_limit;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getXianshi_price() {
        return xianshi_price;
    }

    public void setXianshi_price(String xianshi_price) {
        this.xianshi_price = xianshi_price;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }
}
