package com.ruoogle.base.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关操�?
 * 
 * @author wei.chen
 * 
 */
public final class UtilString
{

	/**
	 * 对用户名和密码进行加�?
	 * @param aUserID:用户�?
	 * @param aPsw:密码
	 * @return 加密后的字符�?
	 */   
	public static String getEncodingStr(String aUserID,String aPsw){
		String shortStr = "BAB%#*#$AFADAGRW!@$EHJG12D!$$%FDBNSDPF";
		String cKeyLongStr = "ewqruoirjlkvjak239#0ur@#%!9023ujdaslkjfAFDAD@SFASDFRRQEF#$QEFQRVBPIHGY~DGHYTU+_)_9SGAR#Q$#@DFGTHETR$#QEDWRET*^$UJNJULO*&IKUJYHTGRFEDS*(&MUNb;pqfwqla;bngs;aw-bpewdewwjg}3====6&*%$^&3789221"; 
		String resultStr="";
		String cSourceStr = aUserID+shortStr+aPsw;
		int len = cSourceStr.length();
		int totalSum=0;
		int mod = cKeyLongStr.length();
		for (int i=0; i<len; i++) {
			totalSum += cSourceStr.charAt(i);
		}
		for (int i=0; i<len; i++) {
			int value = cSourceStr.charAt(i);
			int geWei = value % 10;
			int shiWei = (value / 10) % 10;
			int encodeValue = cKeyLongStr.charAt(value)+cKeyLongStr.charAt(geWei) * cKeyLongStr.charAt(cKeyLongStr.length() - shiWei - 1);
			int baiWei, qianWei;
			geWei = encodeValue % 10;
			shiWei = (encodeValue /10) % 10;
			baiWei = (encodeValue / 100 ) % 10;
			qianWei = (encodeValue / 1000) % 10;
			if(i % 2 == 0){
				resultStr+= cKeyLongStr.charAt((geWei + totalSum) % mod);
				resultStr+= cKeyLongStr.charAt((shiWei*2 + totalSum) % mod);
				resultStr+= cKeyLongStr.charAt((baiWei*3 + totalSum) % mod);			
				resultStr+= cKeyLongStr.charAt((qianWei*4 + totalSum) % mod);			
			}else {
				resultStr+= cKeyLongStr.charAt((geWei*3 + totalSum) % mod);
				resultStr+= cKeyLongStr.charAt((shiWei*5 + totalSum) % mod);
				resultStr+= cKeyLongStr.charAt((baiWei*7 + totalSum) % mod);	
			}
		}
		return resultStr;
	}
	
	/**
	 * 判断value是否以values中的任何�?��结尾，忽略大小写
	 * 
	 * @param value
	 * @param values
	 * @return 如果两个参数中任何一个为null,则返回false
	 */
	public static boolean isEndwith(String value, String[] values)
	{
		if (value == null || values == null)
		{
			return false;
		}

		for (String tmp : values)
		{
			if (value.toLowerCase().endsWith(tmp.toLowerCase()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断value是否与values中的任何�?��相等，忽略大小写
	 * 
	 * @param value
	 * @param values
	 * @return 如果两个参数中任何一个为null,则返回false
	 */
	public static boolean isEqualsIgnoreCase(String value, String[] values)
	{
		if (value == null || values == null)
		{
			return false;
		}

		for (String tmp : values)
		{
			if (value.equalsIgnoreCase(tmp))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * 将字符串转化为最大显示的个数，多余的部分用指定的字符串代�?br/>
	 * 如果字符串本�?=�?��显示个数，则不转�?br/>
	 * �?"我是�?��字符�?，最多显�?个，多余部分�?..."代替<br/>
	 * 转换后则变成:"我是�?.."
	 * 
	 * @param aStr
	 *            要转换的字符�?
	 * @param aMaxCount
	 *            �?��显示个数
	 * @param aReplace
	 *            替换的字符串
	 * @param aShowCount
	 *            大于�?��显示个数时显示几个字�?
	 * @return 如果传入的字符串为null，则返回null<br/>
	 *         不为null则进行转�?
	 */
	public static String getStringMaxCountShow(String aStr, int aMaxCount,
			String aReplace, int aShowCount)
	{
		if (aStr == null)
		{
			return null;
		}

		if (aStr.length() <= aMaxCount)
		{
			return aStr;
		}

		return aStr.substring(0, aShowCount) + aReplace;
	}

	/**
	 * 判断字符串是不是非负的浮点数
	 * 
	 * @param aString
	 * @return
	 */
	public static boolean isPFloatNumeric(String aString)
	{
		if (aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		Matcher isPFloat = pattern.matcher(aString);
		if (isPFloat.matches())
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否是非负整数
	 * 
	 * @param aString
	 * @return
	 */
	public static boolean isPNumeric(String aString)
	{
		if (aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher isPNum = pattern.matcher(aString);
		if (isPNum.matches())
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断字符是否是由数字构成的整数，包括负数
	 * 
	 * @param aString
	 * @return
	 */
	public static boolean isNumeric(String aString)
	{
		if (aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher isNum = pattern.matcher(aString);
		if (isNum.matches())
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断注册的信息的是否是手机号
	 * 
	 * @param aString
	 * @return
	 */
	public static boolean isNumericPhone(String aString)
	{
		if (aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("^[1][3-8]+\\d{9}");
		Matcher isNum = pattern.matcher(aString);
		if (!isNum.matches())
		{
			return false;
		}
		return true;
	}

	/**
	 * 判断注册的信息的是否是邮�?
	 * 
	 * @param aString
	 * @return
	 */
	public static boolean isEmail(String aString)
	{
		if (aString == null || aString.length() == 0)
			return false;
		Pattern pattern = Pattern
				.compile("^\\w+@\\w+\\.(\\w{2,3}|\\w{2,3}\\.\\w{2,3})$");
		Matcher isNum = pattern.matcher(aString);
		if (!isNum.matches())
		{
			return false;
		}
		return true;
	}

	public static String inSrmToString(InputStream is)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try
		{
			while ((line = in.readLine()) != null)
			{
				buffer.append(line);
			}
		} catch (IOException e)
		{
			return null;
		}
		return buffer.toString();
	}
	
	/**
     * 判断字符串是否是图片
     * @author shicong
     * */
    public static boolean isPic(String aString)
    {
    	if(aString == null)
    	{
    		return false;
    	}
    	
    	String tmpString = aString.toLowerCase();
    	if(tmpString.endsWith(".jpg")||
    		tmpString.endsWith(".png")||
    		tmpString.endsWith(".bmp")|| 
    		tmpString.endsWith(".gif"))//有图�?
		{
    		return true;
		}
    	return false;
    }
    
    /**
     * 判断字符串是否是视频
     * @author shicong
     * */
    public static boolean isVideo(String aString)
    {
    	if(aString == null)
    	{
    		return false;
    	}
    	
    	String tmpString = aString.toLowerCase();
    	if(tmpString.endsWith(".mov")||
    		tmpString.endsWith(".3gp")||
    		tmpString.endsWith(".mp4")|| 
    		tmpString.endsWith(".3gpp"))
		{
    		return true;
		}
    	return false;
    }
    
    /**
     * 判断字符串是否是声音
     * @author shicong
     * */
    public static boolean isAudio(String aString)
    {
    	if(aString == null)
    	{
    		return false;
    	}
    	
    	String tmpString = aString.toLowerCase();
    	if(tmpString.endsWith(".mp3")||
    		tmpString.endsWith(".wav")||
    		tmpString.endsWith(".midi")|| 
    		tmpString.endsWith(".3gp"))
		{
    		return true;
		}
    	return false;
    }
	
  /**
   * 从一个网络URL地址中得到文件名�?
   * @author shicong
   * @param aURL：网络地�?��例如www.baidu.com/c.txt
   * */
  public static String getFileNameFromURL(String aURL)
  {
	  int index = aURL.lastIndexOf('/');
	  return aURL.substring(index+1, aURL.length());
  }

  /**
   * 通过分号将数据连成一个字符串例如�?23;a23;cc;d233
   * @author shicong
   * */
  public static String getStringConnWithSemicolon(String aStringArray[])
  {
	  StringBuilder tmpStringBuilder = new StringBuilder("");
	  
	  for(int i = 0; i < aStringArray.length; i++)
	  {
		  tmpStringBuilder.append(aStringArray[i] + ";");
	  }
	  String tmpString = tmpStringBuilder.deleteCharAt(tmpStringBuilder.length() - 1).toString();
	  return tmpString;
  }

  public static String getStringConnWithSemicolon(List<String> aStringList)
  {
	  StringBuilder tmpStringBuilder = new StringBuilder("");
	  
	  for(int i = 0; i < aStringList.size(); i++)
	  {
		  tmpStringBuilder.append(aStringList.get(i) + ";");
	  }
	  String tmpString = tmpStringBuilder.deleteCharAt(tmpStringBuilder.length() - 1).toString();
	  return tmpString;
  }
  
  /**
   * 得到通过分号分隔�?��字符�?
   * @author shicong
   * */
  public static String[] getStringArrayConnWithSemicolon(String aString)
  {
	  return aString.split(";");
  }
  
  /**
   * 将Byte数据转换�?6进制�?
   * @author shicong
   * */
  public static String bytesToHexString(byte[] bytes) {
      // http://stackoverflow.com/questions/332079
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < bytes.length; i++) {
          String hex = Integer.toHexString(0xFF & bytes[i]);
          if (hex.length() == 1) {
              sb.append('0');
          }
          sb.append(hex);
      }
      return sb.toString();
  }

}
