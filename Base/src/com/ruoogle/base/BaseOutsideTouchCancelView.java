/**
 * 
 */
package com.ruoogle.base;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

/**
 * @author shicong
 *
 */
public class BaseOutsideTouchCancelView {

	private Context mContext;
	private Animation mViewAnimOut;
	private Animation mParentAnimOut;
	
	public BaseOutsideTouchCancelView(Context context)
	{
		mContext = context;
		mViewAnimOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
		mParentAnimOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
	}
	
	/**
	 * 设置整个点击响应的View和需要消失的View
	 * @author shicong
	 * */
	public void setOutsideAndAnimView(final ViewGroup aParent,final View aView)
	{
		/*外部View的点击事件*/
		aParent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/*View通过动画消失*/
				aView.startAnimation(mViewAnimOut);		
				
				/*parent View通过动画消失*/
				mParentAnimOut.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						aParent.setVisibility(View.GONE);
					}
				});
				aParent.startAnimation(mParentAnimOut);
			}
		});
	}
	
	/**
	 * 设置进入和退出的动画
	 * @author shicong
	 * */
	public void setInAndOutAnimation(int aOutAnimID)
	{
		mViewAnimOut = AnimationUtils.loadAnimation(mContext,aOutAnimID);
	}
	
	public void setInAndOutAnimation(Animation aOutAnim)
	{
		mViewAnimOut = aOutAnim;
	}
}
