package com.ruoogle.base;

import com.ruoogle.base.Util.Debug;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class FrameLayoutNoticeBar extends FrameLayout {

	private final String TAG = "NoticeBarFrameLayout";
	private final boolean DEBUG = true;
	
	private View mViewNoticePop;//通知的View
	private BroadcastReceiver mNoticeBroadcastReceiver;
	private CountDownTimer mNoticeDisapear;//自动消失倒计时
	
	private long NOTICE_DISAPEAR_TIME = 8000;//通知自动消失的时间
	private NoticeBarOnReceive mNoticeBarOnReceive;//收到通知后的回调函数
	
	public interface NoticeBarOnReceive {
		public static String ACTION_BROADCAST_RECEIVER_NOTICE = "com.ruoogle.base.broadcast.notice";//通知
		public void onReceive(Context context, Intent intent);
	}
	
	public FrameLayoutNoticeBar(Context context) {
		super(context);
		NoticePopInit();
	}

	public FrameLayoutNoticeBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		NoticePopInit();
	}

	public FrameLayoutNoticeBar(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		NoticePopInit();
	}

	/**
	 * 弹出式通知的初始化
	 * @author shicong
	 * 2013-2-22
	 */
	private void NoticePopInit()
	{
		/*初始通知消失倒计时*/
		mNoticeDisapear = new CountDownTimer(NOTICE_DISAPEAR_TIME,NOTICE_DISAPEAR_TIME) {
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_top);
				anim.setDuration(1000);
				mViewNoticePop.startAnimation(anim);
				anim.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						mViewNoticePop.setVisibility(View.GONE);
					}
				});
			}
		};
	}
	
	/**
	 * 设置通知的View
	 * @author shicong
	 *
	 * 2013-2-22
	 * @param v
	 */
	public void setNoticeBarView(View v)
	{
		/*得到弹出框*/
		mViewNoticePop = v;
		
		/*添加弹出框*/
		MarginLayoutParams source = new MarginLayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		source.topMargin = 114;
		FrameLayout.LayoutParams lParams = new LayoutParams(source);
		
		addView(mViewNoticePop, lParams);
	}
	
	/**
	 * 收到通知后的回调函数
	 * @author shicong
	 *
	 * 2013-2-22
	 * @param aReceiveCall
	 */
	public void setOnNoticeReceive(NoticeBarOnReceive aReceiveCall)
	{
		mNoticeBarOnReceive = aReceiveCall;
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		Debug.d(TAG, "onAttachedToWindow", DEBUG);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		Debug.d(TAG, "onDetachedFromWindow", DEBUG);
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		// TODO Auto-generated method stub
		super.onVisibilityChanged(changedView, visibility);
		
		switch (visibility) 
		{
			case View.GONE:
				Debug.d(TAG, "onVisibilityChanged View.GONE", DEBUG);
				unregisterNoticeReceiver();
				if(mViewNoticePop != null)
				{
					mViewNoticePop.setVisibility(View.GONE);
				}
				
				if(mNoticeDisapear != null)
				{
					mNoticeDisapear.cancel();
				}
				
				break;
			case View.INVISIBLE:
				Debug.d(TAG, "onVisibilityChanged View.INVISIBLE", DEBUG);
				unregisterNoticeReceiver();
				
				if(mViewNoticePop != null)
				{
					mViewNoticePop.setVisibility(View.GONE);
				}
				
				if(mNoticeDisapear != null)
				{
					mNoticeDisapear.cancel();
				}
				
				break;
			case View.VISIBLE:
				Debug.d(TAG, "onVisibilityChanged View.VISIBLE", DEBUG);
				registerNoticeReceiver();
				break;
			default:
				break;
		}
	}
	
	/**
	 * 注册通知的监听广播
	 * @author shicong
	 */
	private void registerNoticeReceiver()
	{
		mNoticeBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				
				/*判断是否已经设计了View*/
				Debug.d(TAG, "receive broadcase of notice ...View = " + mViewNoticePop, DEBUG);

				if(mViewNoticePop == null)
				{
					return;
				}
				
				/*显示通知*/
				mViewNoticePop.setVisibility(View.VISIBLE);
				mViewNoticePop.bringToFront();
				
				/*启动通知自动消失倒计时*/
				mNoticeDisapear.start();
				
				/*回到收到通知的处理*/
				if(mNoticeBarOnReceive != null)
				{
					mNoticeBarOnReceive.onReceive(context, intent);
				}
			}
		};
		IntentFilter filter = new IntentFilter(NoticeBarOnReceive.ACTION_BROADCAST_RECEIVER_NOTICE);
		LocalBroadcastManager.getInstance(getContext()).registerReceiver(mNoticeBroadcastReceiver, filter);
	}
	
	/**
	 * 广播的取消注册
	 * @author shicong
	 */
	private void unregisterNoticeReceiver()
	{
		if(mNoticeBroadcastReceiver != null)
		{
			LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mNoticeBroadcastReceiver);
		}
	}
}
