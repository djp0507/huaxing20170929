package cn.hx.hn.android.bean;

public class Store_desccredit {
	private String text;

	private String credit;

	public void setText(String text){
	this.text = text;
	}
	public String getText(){
	return this.text;
	}
	public void setCredit(String credit){
	this.credit = credit;
	}
	public String getCredit(){
	return this.credit;
	}
	@Override
	public String toString() {
		return "Store_desccredit [text=" + text + ", credit=" + credit + "]";
	}
	
}
