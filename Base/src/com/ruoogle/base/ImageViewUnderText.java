/**
 * 
 */
package com.ruoogle.base;


import com.ruoogle.base.Util.UtilSystem;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author shicong
 *
 */
public class ImageViewUnderText extends LinearLayout {

	private float mImageWidth;
	private float mImageHeight;
	private float mFontSize;
	private String mText;
	private int mTextColor;
	private int mImageID;
	private TextView mViewText;
	private ImageView mImageView;
	
	/**
	 * @param context
	 * @param attrs
	 */
	public ImageViewUnderText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context,attrs);
	}
	
	/**
	 * @param context
	 */
	public ImageViewUnderText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context,null);
	}
	
	
	private void init(Context context, AttributeSet attrs)
	{
		if(attrs == null)
		{
			return;
		}
		TypedArray  typeArray = context.obtainStyledAttributes(attrs,R.styleable.ImageViewWithUnderText);

		mImageWidth = typeArray.getDimension(R.styleable.ImageViewWithUnderText_ImageWidth, LayoutParams.WRAP_CONTENT);
		mImageHeight = typeArray.getDimension(R.styleable.ImageViewWithUnderText_ImageHeight, LayoutParams.WRAP_CONTENT);
		mFontSize = typeArray.getDimensionPixelSize(R.styleable.ImageViewWithUnderText_TextFontSize, 20);
		mTextColor = typeArray.getColor(R.styleable.ImageViewWithUnderText_TextColor, 0xFFFFFFFF);
		mText = typeArray.getString(R.styleable.ImageViewWithUnderText_Text);
		mImageID = typeArray.getResourceId(R.styleable.ImageViewWithUnderText_ImageSrc,0);
		
		typeArray.recycle();
		
		/*设置Layout*/
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER_HORIZONTAL);

		/*设置图片*/
		mImageView = new ImageView(context);
		mImageView.setImageResource(mImageID);
		addView(mImageView,(int)mImageWidth,(int)mImageHeight);

		/*设置Text*/
		mViewText = new TextView(context);
		mViewText.setGravity(Gravity.CENTER);
		mViewText.setText(mText);
		mViewText.setTextColor(mTextColor);
		mViewText.setTextSize((new UtilSystem(getContext())).px2dp(mFontSize));
		addView(mViewText,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
	}
	
	/**
	 * 得到ImageView
	 * @return
	 */
	public ImageView getImageView()
	{
		return mImageView;
	}
	
	/**
	 * 得到TextView
	 * @return
	 */
	public TextView getTextView()
	{
		return mViewText;
	}
	
	/**
	 * 设置字符串
	 * @author shicong
	 * */
	public void setText(String aText)
	{
		mViewText.setText(aText);
	}

	public void setText(int aTextResID)
	{
		mViewText.setText(aTextResID);
	}
}
