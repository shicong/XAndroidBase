/**
 * 
 */
package com.ruoogle.base.Util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.ruoogle.base.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author shicong
 *
 */
public class UtilSystem {
	
	private static final String TAG = "UtilSystem";
	private static final boolean DEBUG = true;

	Context mContext;
	public UtilSystem(Context context)
	{
		mContext = context;
	}
	
	/**
	 * 当数据单位从PX转换成DP
	 * @author shicong
	 * */
	public float px2dp(float aPXSize)
	{
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics(); 
		return aPXSize/dm.density;
	}
	
	/**
	 * 得到系统解析图片的参�?
	 * @author shicong
	 * @param aType:系统类型
	 * */
	public static BitmapFactory.Options getSystemBitmapDecodeOpts(int aType)
	{
		BitmapFactory.Options sOpts = new BitmapFactory.Options();
		sOpts.inScaled = false;
		sOpts.inPurgeable = true;
		sOpts.inPreferredConfig = Bitmap.Config.ALPHA_8;
		return sOpts;
	}
	
	/**
	 * 得到系统解析图片的参�?
	 * @author shicong
	 * @param aType:系统类型
	 * @param aScaledToW：缩放后的宽�?
	 * @param aScaledToH：缩放后的高�?
	 * */
	public static BitmapFactory.Options getBitmapDecodeOpts(File aFile,
														  float aScaledToW,
														  float aScaledToH)
	{
		BitmapFactory.Options sOpts = new BitmapFactory.Options();
		sOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(aFile.getAbsolutePath(), sOpts);
		
		sOpts.inSampleSize = (int) Math.max(sOpts.outWidth/aScaledToW, sOpts.outHeight/aScaledToH);
		sOpts.inJustDecodeBounds = false;
		sOpts.inScaled = false;
		sOpts.inPurgeable = true;
		sOpts.inPreferredConfig = Bitmap.Config.ALPHA_8;
		return sOpts;
	}

	public static BitmapFactory.Options getBitmapDecodeOpts(Context context,
														  int aResID,
														  int aScaledToW,
														  int aScaledToH)
	{
		BitmapFactory.Options sOpts = new BitmapFactory.Options();
		sOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), aResID, sOpts);

		sOpts.inSampleSize = Math.max(sOpts.outWidth/aScaledToW, sOpts.outHeight/aScaledToH);
		sOpts.inJustDecodeBounds = false;
		sOpts.inScaled = false;
		sOpts.inPurgeable = true;
		sOpts.inPreferredConfig = Bitmap.Config.ALPHA_8;
		return sOpts;
	}

	/**
	 * 如果宽大于高，图片将被旋转，使宽始终小于�?
	 * 在保证此前提下，得到图片的解析参�?
	 * @author shicong
	 * */
	public static BitmapFactory.Options getBitmapRotationDecodeOpts(File aFile,
																	int aScaledToW,
																	int aScaledToH)
	{
		BitmapFactory.Options sOpts = new BitmapFactory.Options();
		sOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(aFile.getAbsolutePath(), sOpts);
		
		if(sOpts.outWidth > sOpts.outHeight)
		{
			sOpts.inSampleSize = Math.max(sOpts.outHeight/aScaledToW, sOpts.outWidth/aScaledToH);
		}else{
			sOpts.inSampleSize = Math.max(sOpts.outWidth/aScaledToW, sOpts.outHeight/aScaledToH);
		}
		
		sOpts.inJustDecodeBounds = false;
		sOpts.inScaled = false;
		sOpts.inPurgeable = true;
		sOpts.inPreferredConfig = Bitmap.Config.ALPHA_8;
		return sOpts;
	}

	public static BitmapFactory.Options getBitmapRotationDecodeOpts(Context context,
																	int aResID,
																	int aScaledToW,
																	int aScaledToH)
	{
		BitmapFactory.Options sOpts = new BitmapFactory.Options();
		sOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), aResID, sOpts);

		if(sOpts.outWidth > sOpts.outHeight)
		{
			sOpts.inSampleSize = Math.max(sOpts.outHeight/aScaledToW, sOpts.outWidth/aScaledToH);
		}else{
			sOpts.inSampleSize = Math.max(sOpts.outWidth/aScaledToW, sOpts.outHeight/aScaledToH);
		}
		
		sOpts.inJustDecodeBounds = false;
		sOpts.inScaled = false;
		sOpts.inPurgeable = true;
		sOpts.inPreferredConfig = Bitmap.Config.ALPHA_8;
		return sOpts;
	}
	
	public static BitmapFactory.Options getBitmapRotationDecodeOpts(Bitmap aBitmap,
																	int aScaledToW,
																	int aScaledToH)
	{
		BitmapFactory.Options sOpts = new BitmapFactory.Options();
		int sBitmapW = aBitmap.getWidth();
		int sBitmapH = aBitmap.getHeight();
		if(sBitmapW > sBitmapH)
		{
			sOpts.inSampleSize = Math.max(sBitmapH/aScaledToW, sBitmapW/aScaledToH);
		}else{
			sOpts.inSampleSize = Math.max(sBitmapW/aScaledToW, sBitmapH/aScaledToH);
		}
		
		sOpts.inJustDecodeBounds = false;
		sOpts.inScaled = false;
		sOpts.inPurgeable = true;
		sOpts.inPreferredConfig = Bitmap.Config.ALPHA_8;
		return sOpts;
	}
	
	/**
	 * 得到数据加载的Dialog
	 * @author shicong
	 * */
	public static ProgressDialog getPopDataLoadProgressDialog(Context contex)
	{
		ProgressDialog sDialog = new ProgressDialog(contex);
		sDialog.setIndeterminate(true);
		sDialog.setMessage(contex.getResources().getString(R.string.Str_Base_Load));
		sDialog.setCanceledOnTouchOutside(false);
		
		return sDialog;
	}
	
	/**
	 * 弹出�?��用户确定的Dialog
	 * @author shicong
	 * @param context
	 * @param aMessageResID:提示文字的字符串ID
	 * @param aBtnTitle：按钮上的提示文字的字符串ID
	 * */
	public static void getUserOKDialog(Context context,
										int aMessageResID,
										int aBtnTitle,
										final DialogInterface.OnClickListener aBtnClickListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(aMessageResID)
		       .setCancelable(true)
		       .setPositiveButton(aBtnTitle, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(aBtnClickListener != null)
					{
						aBtnClickListener.onClick(dialog, which);
					}
				}
			});
		builder.create().show();		
	}
	
	public static void getUserOKDialog(Context context,
										String aMessage,
										int aBtnTitle,
										final DialogInterface.OnClickListener aBtnClickListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(aMessage)
		       .setCancelable(true)
		       .setPositiveButton(aBtnTitle, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		        	   if(aBtnClickListener != null)
		        	   {
		        		   aBtnClickListener.onClick(dialog, id);
		        	   }
		           }
		       });
		builder.create().show();		
	}
	
	public static void getUserOKDialog(Context context,
										int aMessageResID,
										final DialogInterface.OnClickListener aBtnClickListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(aMessageResID)
		       .setCancelable(true)
		       .setPositiveButton(R.string.Str_Base_I_See, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		        	   if(aBtnClickListener != null)
		        	   {
		        		   aBtnClickListener.onClick(dialog, id);
		        	   }
		           }
		       });
		builder.create().show();		
	}
	
	public static void getUserOKDialog(Context context,
										String aMessage,
										final DialogInterface.OnClickListener aBtnClickListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(aMessage)
		       .setCancelable(true)
		       .setPositiveButton(R.string.Str_Base_I_See, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		        	   if(aBtnClickListener != null)
		        	   {
		        		   aBtnClickListener.onClick(dialog, id);
		        	   }
		           }
		       });
		builder.create().show();		
	}
	
	
	/**
	 * 系统�?��用户选择是否的提示框
	 * @author shicong
	 * */
	public static void getUserOKAndCancelDialog(Context context,
												int aMessageID,
												int aResIDOKBtnTitle,
												final DialogInterface.OnClickListener aOkBtnClickListener,
												int aResIDCancelBtnTitle,
												final DialogInterface.OnClickListener aCancelBtnClickListener)
	{
		String Message = context.getResources().getString(aMessageID);
		getUserOKAndCancelDialog(context, Message, aResIDOKBtnTitle, aOkBtnClickListener, aResIDCancelBtnTitle, aCancelBtnClickListener);
	}
	
	public static void getUserOKAndCancelDialog(Context context,
												String aMessage,
												int aResIDOKBtnTitle,
												final DialogInterface.OnClickListener aOkBtnClickListener,
												int aResIDCancelBtnTitle,
												final DialogInterface.OnClickListener aCancelBtnClickListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(aMessage)
		       .setCancelable(true)
		       .setPositiveButton(aResIDOKBtnTitle, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if(aOkBtnClickListener != null)
						{
							aOkBtnClickListener.onClick(dialog, which);
						}
					}
				})
		       .setNegativeButton(aResIDCancelBtnTitle, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if(aCancelBtnClickListener != null)
						{
							aCancelBtnClickListener.onClick(dialog, which);
						}
					}
		       });
		builder.create().show();		
	}
	/**
	 *系统弹出的提示框
	 *@author shicong 
	 *@param context
	 *@param aString:提示信息
	 * */
	public static void SystemToast(Context context,String aString)
	{
		Toast tmpToast = Toast.makeText(context, aString, Toast.LENGTH_SHORT);
		tmpToast.setGravity(Gravity.CENTER, 0, 0);
		tmpToast.show();
	}

	public static void SystemToast(Context context,int aResId)
	{
		Toast tmpToast = Toast.makeText(context, aResId, Toast.LENGTH_SHORT);
		tmpToast.setGravity(Gravity.CENTER, 0, 0);
		tmpToast.show();
	}
	
	/**
	* 根据格式和日期获取按格式显示的日�?
	* @param sFormat 格式
	* @param date    日期
	 * @throws ParseException 
	*/
	  public static Date getFormateDate(String sFormat, String date) throws ParseException
	  {
	    if ((date == null) || ("".equals(date)))
	      return null;
	
	    if ((sFormat.equals("yy")) || 
	    	(sFormat.equals("yyyy")) || 
	    	(sFormat.equals("MM")) || 
	    	(sFormat.equals("dd")) || 
	    	(sFormat.equals("MM/dd")) || 
	    	(sFormat.equals("yyyyMM")) || 
	    	(sFormat.equals("yyyyMMdd")) || 
	    	(sFormat.equals("yyyy/MM")) || 
	    	(sFormat.equals("yy/MM/dd")) || 
	    	(sFormat.equals("yyyy/MM/dd")) || 
	    	(sFormat.equals("yyyy-MM-dd")) || 
	    	(sFormat.equals("HH:mm")) || 
	    	(sFormat.equals("yy/MM/dd HH:mm")) ||                
	    	(sFormat.equals("yyyy/MM/dd HH:mm:ss")) ||  
	    	(sFormat.equals("yyyy-MM-dd hh:mm:ss")) || 
	    	(sFormat.equals("yyyy-MM-dd HH:mm:ss")) ||
	    	(sFormat.equals("MM-dd HH:mm")) ||
	    	(sFormat.equals("yyyyMMddHHmm")) || 
	    	(sFormat.equals("yyyyMMddHHmmss")) || 
	    	(sFormat.equals("yyyyMMdd-HHmmss")) || 
	    	(sFormat.equals("yyyy年MM月dd日")) || 
	    	(sFormat.equals("yyyy年MM月")) || 
	    	(sFormat.equals("MM月dd日")) || 
	    	(sFormat.equals("dd日")) || 
	    	(sFormat.equals("HH")) || 
	    	(sFormat.equals("mm"))||
	    	(sFormat.equals("yyyy-MM-dd'T'HH:mm:ssZZZ"))) 
	    {
	      SimpleDateFormat formatter = new SimpleDateFormat(sFormat);
	      return formatter.parse(date);
	    }
	    return null;
	  }
	  
	/**
	 * 格式化时间为Feed时间
	 * @author shicong
	 * @param aStrDate:输入时间字符�?
	 * */
//	public static String formateFeedTime(String aStrDate) 
//	{
//		return formateTime("yyyy-MM-dd HH:mm:ss",aStrDate);
//	}

	public static String getFormateTime(String aStrFormat,String aStrDate) 
	{
		Debug.d(TAG, "Formate time = " + aStrDate, DEBUG);
		
		Date sFeedDate;
		try {
			sFeedDate = getFormateDate(aStrFormat, aStrDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

		Calendar nowCalendar = Calendar.getInstance();
		int oneDayInterval = 60 * 60 * 24;//�?��的秒�?
		
		/*过滤通过取整过滤掉今天的时间*/
		long intervalDays = nowCalendar.getTimeInMillis()/1000/oneDayInterval;
		long intervalToTodayBegin = intervalDays * oneDayInterval - sFeedDate.getTime()/1000;
		long intervalToNow = (nowCalendar.getTimeInMillis() - sFeedDate.getTime())/ 1000;// 持续的秒�?

		String sFormatterString = "";
		if(intervalToNow < 0)//帖子在未�?....
		{
			return "";
		}
		else if (intervalToTodayBegin <= 0) {//今天
			if (intervalToNow < 60 * 60 && intervalToNow >= 60 * 1) {
				return String.format("%d 分钟, (int) intervalToNow / 60");
			} else if (intervalToNow < 60 * 1) {
				return "刚才";
			} else
				sFormatterString = "今日 HH:mm";
		} else if (intervalToTodayBegin < oneDayInterval) {//昨天
			sFormatterString = "昨日 HH:mm";
		} else if (intervalToTodayBegin < 2*oneDayInterval) {//前天
			sFormatterString = "前日 HH:mm";
		} else {
			sFormatterString = "MM-dd HH:mm";
		}
		
		SimpleDateFormat sFormatter = new SimpleDateFormat(sFormatterString);
		String RetString = sFormatter.format(sFeedDate);
		return RetString;

	}
	/**
	 * 格式化时间为帖子大厅列表时间
	 * @author shicong
	 * @param aStrDate:输入时间字符�?
	 * */
	public static String formateTime2FamilyFeedsTime(String aTime) 
	{
		return formateHallNotesTime("yyyy-MM-dd",aTime);
	}
	
	/**
	 * 格式化时间为通知列表时间
	 * @author shicong
	 * @param aStrDate:输入时间字符�?
	 * */
	public static String formateTime2NoticeTime(String aTime)
	{
		return formateHallNotesTime("yyyy-MM-dd",aTime);
	}
	
	private static String formateHallNotesTime(String aStrFormat,String aStrDate) 
	{
		Debug.d(TAG, "Formate time = " + aStrDate, DEBUG);
		
		Date sFeedDate;
		try {
			sFeedDate = getFormateDate(aStrFormat, aStrDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

		Calendar nowCalendar = Calendar.getInstance(TimeZone.getDefault());
		int oneDayInterval = 60 * 60 * 24;//�?��的秒�?
		
		/*过滤通过取整过滤掉今天的时间*/
		long intervalDays = nowCalendar.getTimeInMillis()/1000/oneDayInterval;
		long intervalToTodayBegin = intervalDays * oneDayInterval - sFeedDate.getTime()/1000;
		long intervalToNow = (nowCalendar.getTimeInMillis() - sFeedDate.getTime())/ 1000;// 持续的秒�?

		String sFormatterString = "";
		if(intervalToNow < 0)//帖子在未�?....
		{
			return "";
		}
		else if (intervalToTodayBegin <= 0) {//今天
			if (intervalToNow < 60 * 60 && intervalToNow >= 60 * 1) {
				return String.format("%d分钟, (int) intervalToNow / 60");
			} else if (intervalToNow < 60 * 1) {
				return "刚才";
			} else
				sFormatterString = "HH:mm";
		} else if (intervalToTodayBegin < oneDayInterval) {//昨天
			return "昨日";
		} else if (intervalToTodayBegin < 2*oneDayInterval) {//前天
			return "前日";
		} else {
			sFormatterString = "MM月dd日";
		}
		
		SimpleDateFormat sFormatter = new SimpleDateFormat(sFormatterString);
		String RetString = sFormatter.format(sFeedDate);
		return RetString;

	}

}
