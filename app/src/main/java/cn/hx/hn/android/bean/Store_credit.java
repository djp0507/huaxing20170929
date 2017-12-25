package cn.hx.hn.android.bean;

public class Store_credit {
	private Store_desccredit store_desccredit;

	private Store_servicecredit store_servicecredit;

	private Store_deliverycredit store_deliverycredit;

	public void setStore_desccredit(Store_desccredit store_desccredit){
	this.store_desccredit = store_desccredit;
	}
	public Store_desccredit getStore_desccredit(){
	return this.store_desccredit;
	}
	public void setStore_servicecredit(Store_servicecredit store_servicecredit){
	this.store_servicecredit = store_servicecredit;
	}
	public Store_servicecredit getStore_servicecredit(){
	return this.store_servicecredit;
	}
	public void setStore_deliverycredit(Store_deliverycredit store_deliverycredit){
	this.store_deliverycredit = store_deliverycredit;
	}
	public Store_deliverycredit getStore_deliverycredit(){
	return this.store_deliverycredit;
	}
	@Override
	public String toString() {
		return "Store_credit [store_desccredit=" + store_desccredit
				+ ", store_servicecredit=" + store_servicecredit
				+ ", store_deliverycredit=" + store_deliverycredit + "]";
	}
	
}
