/**
 * 
 */
package com.ruoogle.base.Util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;

/**
 * @author shicong
 *
 */
public class UtilAnimation {
	
    /**
     * 得到平移动画
     * @author shicong
     * @return 返回动画
     * */
    public static TranslateAnimation getTranslateAnimation(
    													float fromXDelta, 
    													float toXDelta, 
    													float fromYDelta, 
    													float toYDelta,
    													long durationMillis,
    													long startOffset,
    													Interpolator i)

    {
    	TranslateAnimation mTranAnima = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        mTranAnima.setDuration(durationMillis);
        mTranAnima.setStartOffset(startOffset);
        mTranAnima.setFillAfter(true);
        mTranAnima.setInterpolator(i);
        return mTranAnima;
    }

    
    /**
     * 得到旋转的动�?
     * @author shicong
     * */
    public static RotateAnimation getSelfRotateAnimation(
    													float fromDegrees, 
    													float toDegrees,
    													long durationMillis,
    													long startOffset,
    													Interpolator i)
    {
    	RotateAnimation animation = new RotateAnimation(
										    			fromDegrees, 
										    			toDegrees,
														Animation.RELATIVE_TO_SELF,
														0.5f,
														Animation.RELATIVE_TO_SELF,
														0.5f);
		animation.setDuration(durationMillis);
		animation.setStartOffset(startOffset);
		animation.setFillAfter(true);
		animation.setInterpolator(i);
		return animation;
    }
    
}
