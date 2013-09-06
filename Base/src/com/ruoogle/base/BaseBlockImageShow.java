/**
 * 
 */
package com.ruoogle.base;

import java.io.File;
import java.util.List;

import com.ruoogle.base.Util.UtilSystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;

/**
 * @author shicong
 *
 */
public class BaseBlockImageShow {

	private Context mContext;
	private int mVW = 280,mVH = 280;
	
	int sArrayWH[][];//记录每个宫格图片的宽�?
	int sArrayPos[][];//记录每个宫格图片的位�?
	int sArrayMaxLen;//�?��保护的图片数�?
	
	public BaseBlockImageShow(Context context)
	{
		mContext = context;
	}
	
	/**
	 * 设置ImageView的属�?
	 * @author shicong
	 * */
	public void setImageViewProperty(int aWidth,int aHeight)
	{
		mVW = aWidth;
		mVH = aHeight;
		
	}
	
	/**
	 * 设置照片
	 * @author shicong
	 * @param aBitmapList:ImageView�?��要的照片列表图片
	 * */
	public LayerDrawable getImageViewBitmapLayer(List<Bitmap> aBitmapList)
	{
		initImageProperty();
		
		int sCount = aBitmapList.size() > sArrayMaxLen?sArrayMaxLen:aBitmapList.size();
		
		Drawable[] layers = new Drawable[sCount];

		/*数据的读取位*/
		int sStartPos = sCount * (sCount - 1)/2;
		
		for (int i = 0; i < sCount; i++)
		{
		    layers[i] = new BitmapDrawable(aBitmapList.get(i)); 
		}

		/*得到LayerDawable*/
		LayerDrawable layerDrawable = new LayerDrawable(layers);
		for (int i = 0; i < sCount; i++)
		{
			int layerLeft = sArrayPos[sStartPos + i][0];
			int layerTop = sArrayPos[sStartPos + i][1];
			int layerRight = sArrayPos[sStartPos + i][2];
			int layerBottom = sArrayPos[sStartPos + i][3];
			
		    layerDrawable.setLayerInset(i, layerLeft, layerTop, layerRight, layerBottom);
		}

		return layerDrawable;
//		aViewPhoto.setImageDrawable(layerDrawable);		
	}

	/**
	 * 初始化图片的参数数组
	 * @author shicong
	 * */
	private void initImageProperty()
	{
		sArrayWH = new int[][]{
				{mVW,mVH},
				{mVW/2,mVH},{mVW/2,mVH},
				{mVW/2,mVH/2},{mVW/2,mVH/2},{mVW/2,mVH},
				{mVW/2,mVH/2},{mVW/2,mVH/2},{mVW/2,mVH/2},{mVW/2,mVH/2},
		};
		sArrayPos = new int[][]{
				{0,0,0,0},
				{0,0,mVW/2,0},{mVW/2,0,0,0},
				{0,0,mVW/2,mVH/2},{0,mVH/2,mVW/2,0},{mVW/2,0,0,0},
				{0,0,mVW/2,mVH/2},{0,mVH/2,mVW/2,0},{mVW/2,0,0,mVH/2},{mVW/2,mVH/2,0,0},
		};	
		
		sArrayMaxLen = sArrayWH.length;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int[][] getImageLayerDrawableWH(int aVW,int aVH)
	{
		return  new int[][]{
				{aVW,aVH},
				{aVW/2,aVH},{aVW/2,aVH},
				{aVW/2,aVH/2},{aVW/2,aVH/2},{aVW/2,aVH},
				{aVW/2,aVH/2},{aVW/2,aVH/2},{aVW/2,aVH/2},{aVW/2,aVH/2}};
	}
	
	/**
	 * 得到显示图片的Bitmap drawable
	 * @author shicong
	 * @param aResID：需要显示的图片资源
	 * @param w：宽
	 * @param h:�?
	 * */
	private Drawable getResDrawable(int aResID,int w,int h)
	{
		BitmapFactory.Options sOpts = UtilSystem.getBitmapRotationDecodeOpts(mContext, 
																			aResID,
																			w, 
																			h);
		Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), aResID,sOpts);
		BitmapDrawable bmd;
		if(sOpts.outWidth > sOpts.outHeight)
		{
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap tmpBitmap = Bitmap.createBitmap(bm, 
												   0, 
												   0, 
												   sOpts.outWidth, 
												   sOpts.outHeight, 
												   matrix, 
												   true);
			bmd = new BitmapDrawable(tmpBitmap);
			bm.recycle();
		}else{
			bmd = new BitmapDrawable(bm);
		}
		return bmd;
	}
	
	/**
	 * 裁剪得到相应尺寸的照�?
	 * @author shicong
	 * @param aBitmap：需要做Scaled的图�?
	 * @param w：裁剪后的宽
	 * @param h：裁剪后的高
	 * */
	private Drawable getScaledDrawable(Bitmap aBitmap,int w,int h)
	{
		Bitmap tmpBitmap = Bitmap.createScaledBitmap(aBitmap, w, h, true);
		BitmapDrawable tmpDrawable = new BitmapDrawable(tmpBitmap);
		aBitmap.recycle();

		return tmpDrawable;
	}
	
	/**
	 * 得到旋转后的照片后的图片
	 * @author shicong
	 * @param aBitmap：需要旋转的图片
	 * @param aDegree：旋转的角度
	 * */
	private Drawable getRotationDrawable(Bitmap aBitmap,float aDegree)
	{
		BitmapDrawable bmd;
		
		int sBMWidth = aBitmap.getWidth();
		int sBMHeight = aBitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.setRotate(aDegree);
		Bitmap tmpBitmap = Bitmap.createBitmap(aBitmap,0,0,sBMWidth,sBMHeight,matrix,true);
		bmd = new BitmapDrawable(tmpBitmap);
		aBitmap.recycle();

		return bmd;
	}
}
