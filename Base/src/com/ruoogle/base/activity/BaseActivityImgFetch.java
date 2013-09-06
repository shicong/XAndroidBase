package com.ruoogle.base.activity;


import com.ruoogle.base.R;
import com.ruoogle.base.imgload.ImageCache.ImageCacheParams;
import com.ruoogle.base.imgload.ImageFetcher;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivityImgFetch extends FragmentActivity {

	private ImageFetcher mImageFetcher;//获取图片的接口
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mImageFetcher = createImageFetcher();
	}
	
	/**
	 * 得到图片获取模块
	 * @param aResId:当读取不到图片时的背景图片
	 * @param aImgThumbWidth：图片resize后的宽
	 * @param aImgThumbHeight：图片resize后的高
	 * @return
	 */
	protected ImageFetcher createImageFetcher() 
	{
		/*初始化异步加载资源*/
        ImageCacheParams cacheParams = new ImageCacheParams(this, "cache");

        // Set memory cache to 25% of mem class
        cacheParams.setMemCacheSizePercent(this, 0.25f);

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(this, 280,280);
        mImageFetcher.setLoadingImage(R.drawable.pic_loading);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);

        mImageFetcher.setImageFadeIn(false);

        return mImageFetcher;
	}
	
	/**
	 * 返回获取网络图片的模块
	 * @return
	 */
	public ImageFetcher getImageFetcher()
	{
		return mImageFetcher;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mImageFetcher.closeCache();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}
}
