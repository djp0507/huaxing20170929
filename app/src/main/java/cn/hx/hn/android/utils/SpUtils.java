package cn.hx.hn.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.hx.hn.android.common.Constants;


/**
 * 首选项工具类
 * @author zhanggongyi
 *
 */
public class SpUtils {
	private static final String SP_NAME = Constants.SYSTEM_INIT_FILE_NAME;
	/**
	 * 写入首选项
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writeSp(Context context,String key,Object value){
		SharedPreferences sp = getSp(context);
		if (value instanceof Integer) {
			sp.edit().putInt(key, (Integer) value).commit();
		}else if (value instanceof Boolean) {
			sp.edit().putBoolean(key,  (Boolean) value).commit();
		}else if(value instanceof String){
			sp.edit().putString(key,(String) value).commit();
		}
	}
	
	/**
	 * 获取boolean类型的首选项
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getSpBoolean(Context context,String key){
		SharedPreferences sp = getSp(context);
		boolean value = sp.getBoolean(key, false);
		return value;
	}
	
	/**
	 * 获取String类型的首选项
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getSpString(Context context,String key){
		SharedPreferences sp = getSp(context);
		String value = sp.getString(key, "");
		return value;
		
	}
	
	/**
	 * 获取Integer类型的首选项
	 * @param context
	 * @param key
	 * @return
	 */
	public static Integer getSpInteger(Context context,String key){
		SharedPreferences sp = getSp(context);
		Integer value = sp.getInt(key, 0);
		return value;
	}
	
	private static SharedPreferences getSp(Context context){
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		return sp;
	} 
}
