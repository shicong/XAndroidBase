package com.ruoogle.base.Util;

import android.util.Log;

public class Debug {

	public static int d(String tag,String msg,boolean debug)
	{
		if(debug)
		{
			return Log.d(tag, msg);
		}
		return -1;
	}
	
	public static int e(String tag,String msg,boolean debug)
	{
		if(debug)
		{
			return Log.e(tag, msg);
		}
		return -1;
	}

	public static int w(String tag,String msg,boolean debug)
	{
		if(debug)
		{
			return Log.w(tag, msg);
		}
		return -1;
	}

	public static int i(String tag,String msg,boolean debug)
	{
		if(debug)
		{
			return Log.i(tag, msg);
		}
		return -1;
	}

	public static int v(String tag,String msg,boolean debug)
	{
		if(debug)
		{
			return Log.v(tag, msg);
		}
		return -1;
	}
}
