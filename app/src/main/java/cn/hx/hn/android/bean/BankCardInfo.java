package cn.hx.hn.android.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/20 0020.
 * 绑定的银行卡或支付宝的信息
 */

public class BankCardInfo implements Serializable {
    String name;
    String number;

    public BankCardInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
