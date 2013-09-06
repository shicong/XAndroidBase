/**
 * 
 */
package com.ruoogle.base;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author shicong
 *
 */
public class ImageViewContour extends ImageView {

 	private Path mPath;
	private Paint mPaint;
	
	private float mViewWidth = 0l;
	private float mViewHeight = 0l;
	private float mViewRadius = 0l;
	private int mViewContourColor = 0x00ffffff;
	
	
	private final int IMAGEVIEW_TYPE_NONE = 0;
	private final int IMAGEVIEW_TYPE_CIRCLE = 1;
	private final int IMAGEVIEW_TYPE_RECTF = 2;
	private int mImgViewType = IMAGEVIEW_TYPE_CIRCLE;
	
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ImageViewContour(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
		TypedArray  typeArray = context.obtainStyledAttributes(attrs,R.styleable.ImageViewWithContour);
		if(typeArray.hasValue(R.styleable.ImageViewWithContour_ContourType))
		{
			mImgViewType = typeArray.getInteger(R.styleable.ImageViewWithContour_ContourType,IMAGEVIEW_TYPE_CIRCLE);
		}
			
		mViewWidth = typeArray.getDimension(R.styleable.ImageViewWithContour_ContourWidth, 0);
		mViewHeight = typeArray.getDimension(R.styleable.ImageViewWithContour_ContourHeight, 0);
		mViewRadius = typeArray.getDimension(R.styleable.ImageViewWithContour_ContourRadius, 0);
		mViewContourColor = typeArray.getColor(R.styleable.ImageViewWithContour_ContourColor, 0x00ffffff);
		
		typeArray.recycle();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ImageViewContour(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
		TypedArray  typeArray = context.obtainStyledAttributes(attrs,R.styleable.ImageViewWithContour);
		if(typeArray.hasValue(R.styleable.ImageViewWithContour_ContourType))
		{
			mImgViewType = typeArray.getInteger(R.styleable.ImageViewWithContour_ContourType,IMAGEVIEW_TYPE_CIRCLE);
		}
		mViewWidth = typeArray.getDimension(R.styleable.ImageViewWithContour_ContourWidth, 0);
		mViewHeight = typeArray.getDimension(R.styleable.ImageViewWithContour_ContourHeight, 0);
		mViewRadius = typeArray.getDimension(R.styleable.ImageViewWithContour_ContourRadius, 0);
		mViewContourColor = typeArray.getColor(R.styleable.ImageViewWithContour_ContourColor, 0x00ffffff);
		typeArray.recycle();
	}

	/**
	 * @param context
	 */
	public ImageViewContour(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
 	}

	/**
	 * 初始化
	 * */
	private void init(Context context)
	{
		mPath = new Path();
        mPaint = new Paint();
	}

	/* (non-Javadoc)
	 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		switch (mImgViewType) 
		{
			case IMAGEVIEW_TYPE_CIRCLE://圆轮廓
	    		mPaint.setStyle(Paint.Style.STROKE);
	    		mPaint.setStrokeWidth(5);
	            mPaint.setColor(mViewContourColor);
	    		canvas.drawCircle(mViewWidth/2, mViewWidth/2, mViewWidth/2, mPaint);

	        	mPath.addCircle(mViewWidth/2, mViewWidth/2, mViewWidth/2, Path.Direction.CCW);
	        	canvas.clipPath(mPath, Region.Op.INTERSECT);
	    		super.onDraw(canvas);
	    		break;
			case IMAGEVIEW_TYPE_RECTF://圆角矩形轮廓
	    		mPaint.setStyle(Paint.Style.STROKE);
	    		mPaint.setStrokeWidth(5);
	            mPaint.setColor(mViewContourColor);
	    		canvas.drawRoundRect(
	    							new RectF(0, 0, mViewWidth, mViewHeight), 
	    							mViewRadius,mViewRadius,
	    							mPaint);
	    		mPath.addRoundRect(
	            					new RectF(0, 0, mViewWidth, mViewHeight), 
	            					mViewRadius,mViewRadius, 
	            					Path.Direction.CCW);
	            canvas.clipPath(mPath, Region.Op.INTERSECT);
	    		super.onDraw(canvas);
				break;
			case IMAGEVIEW_TYPE_NONE:
			default:
				super.onDraw(canvas);
				break;
		}
	}
	
	/**
	 * 设置此View的属性
	 * */
	public void setImageViewProperty(int aViewType,
									float aWidth,
									float aHeight,
									float aRadius,
									int aContourColor)
	{
		mImgViewType = aViewType;
		mViewWidth = aWidth;
		mViewHeight = aHeight;
		mViewRadius = aRadius;
		mViewContourColor = aContourColor;
	}

	/* (non-Javadoc)
	 * @see android.widget.ImageView#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

		switch (mImgViewType) 
		{
			case IMAGEVIEW_TYPE_CIRCLE://圆轮廓
			case IMAGEVIEW_TYPE_RECTF://圆角矩形轮廓
				setMeasuredDimension((int)mViewWidth, (int)mViewHeight);
				break;
			case IMAGEVIEW_TYPE_NONE:
			default:
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				break;
		}
	}
	
}
