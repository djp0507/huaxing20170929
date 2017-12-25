package cn.hx.hn.android.bean;

public class Store_presales {
	private String name;

	private int type;

	private String num;

	public void setName(String name){
	this.name = name;
	}
	public String getName(){
	return this.name;
	}
	public void setType(int type){
	this.type = type;
	}
	public int getType(){
	return this.type;
	}
	public void setNum(String num){
	this.num = num;
	}
	public String getNum(){
	return this.num;
	}
	@Override
	public String toString() {
		return "Store_presales [name=" + name + ", type=" + type + ", num="
				+ num + "]";
	}
	
	
}
