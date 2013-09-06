/**
 * 
 */
package com.ruoogle.base;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author shicong
 *
 */
public class BasePopListMenu {

	private Context mContext;
	private ListView mListView;
	private AlertDialog mDialog;
	private String mTitle;
	private AdapterView.OnItemClickListener mOnItemClickListener;
	
	/**
	 * 构造和初始化字符串
	 * @author shicong
	 * @param context
	 * @param aItemList:Item的字符串列表
	 * */
	public BasePopListMenu(Context context,String []aItemList)
	{
		mContext = context;
		
		ArrayList<HashMap<String, Object>> ItemDataList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < aItemList.length; i++) {  
            HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("Itemtext", aItemList[i]);   
            ItemDataList.add(map);  
        }  
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = (ListView) inflater.inflate(R.layout.base_popup_menu, null);
		SimpleAdapter simpleLarge = new SimpleAdapter(
														mContext, 
														ItemDataList, 
														R.layout.base_popup_menu_item, 
														new String[]{"Itemtext"}, 
														new int[]{R.id.ID_COMM_POPUP_MENU_ITEM});
		mListView.setAdapter(simpleLarge);
	}
	
	/**
	 * 构造和初始化字符串
	 * @author shicong
	 * @param context
	 * @param aItemList:Item的字符串ID列表
	 * */
	public BasePopListMenu(Context context,int []aItemListResID)
	{
		mContext = context;
		
		ArrayList<HashMap<String, Object>> ItemDataList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < aItemListResID.length; i++) {  
            HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("Itemtext", context.getResources().getString(aItemListResID[i]));   
            ItemDataList.add(map);  
        }  
        
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = (ListView) inflater.inflate(R.layout.base_popup_menu, null);
		SimpleAdapter simpleLarge = new SimpleAdapter(
														mContext, 
														ItemDataList, 
														R.layout.base_popup_menu_item, 
														new String[]{"Itemtext"}, 
														new int[]{R.id.ID_COMM_POPUP_MENU_ITEM});
		mListView.setAdapter(simpleLarge);
	}
	
	/**
	 * 注册Item的点击处理函数
	 * @author shicong
	 * */
	public void setPopListItemClickListener(AdapterView.OnItemClickListener listener)
	{
		mOnItemClickListener = listener;
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				dismiss();
				mOnItemClickListener.onItemClick(arg0, arg1, arg2, arg3);
			}
		});
	}
	
	/**
	 * 设置弹出框标题
	 * @author shicong
	 * */
	public void setTitle(int aResID)
	{
		mTitle = mContext.getResources().getString(aResID);
	}
	
	public void setTitle(String aTitle)
	{
		mTitle = aTitle;
	}
	/**
	 * 显示Menu
	 * @author shicong
	 * */
	public void show()
	{
		/*设置弹出的Dialog*/
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setPositiveButton(R.string.Str_Base_Cancel, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		builder.setMessage(mTitle);
		builder.setView(mListView);
		mDialog = builder.create();
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.show();
	}
	
	/**
	 * 关闭PopListMenu
	 * @author shicong
	 * */
	public void dismiss()
	{
		if(mDialog != null)
		{
			mDialog.dismiss();
		}
	}
}
