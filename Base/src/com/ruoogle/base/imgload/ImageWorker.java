/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ruoogle.base.imgload;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.ruoogle.base.BaseBlockImageShow;
import com.ruoogle.base.BuildConfig;

/**
 * This class wraps up completing some arbitrary long running work when loading a bitmap to an
 * ImageView. It handles things like using a memory and disk cache, running the work in a background
 * thread and setting a placeholder image.
 */
public abstract class ImageWorker {
    private static final String TAG = "ImageWorker";
    private static final int FADE_IN_TIME = 200;

    private ImageCache mImageCache;
    private ImageCache.ImageCacheParams mImageCacheParams;
    private Bitmap mLoadingBitmap;
    private boolean mFadeInBitmap = true;
    private boolean mExitTasksEarly = false;
    protected boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();

    protected Resources mResources;

    private static final int MESSAGE_CLEAR = 0;
    private static final int MESSAGE_INIT_DISK_CACHE = 1;
    private static final int MESSAGE_FLUSH = 2;
    private static final int MESSAGE_CLOSE = 3;

    private Context mContext;
    protected ImageWorker(Context context) {
        mResources = context.getResources();
        mContext = context;
    }

    /**
     * Load an image specified by the data parameter into an ImageView (override
     * {@link ImageWorker#processBitmap(Object)} to define the processing logic). A memory and disk
     * cache will be used if an {@link ImageCache} has been set using
     * {@link ImageWorker#setImageCache(ImageCache)}. If the image is found in the memory cache, it
     * is set immediately, otherwise an {@link AsyncTask} will be created to asynchronously load the
     * bitmap.
     *
     * @param data The URL of the image to download.
     * @param imageView The ImageView to bind the downloaded image to.
     */
    public void loadImage(List<String> aURLList, ImageView imageView) {
        
    	/*没有需要下载的数据*/
    	if ((aURLList == null) ||(aURLList.size() == 0))
        {
            return;
        }

        List<String> sURLList = aURLList;//一个Imageview终于需要的图片（分很多层）
        List<Bitmap> sBitmaplist = new ArrayList<Bitmap>();//记录所有URL中的Bitmap列表
        List<String> sNoMemCacheURL = new ArrayList<String>();//所有没有被Memory Cache保存的URL

        /*load bitmap from memory cache*/
        if (mImageCache != null) 
        {
            Bitmap bitmap = null;
        	for(int i = 0; i < sURLList.size(); i++)
        	{
        		bitmap = mImageCache.getBitmapFromMemCache(sURLList.get(i));
        		if(bitmap != null)
        		{
        			sBitmaplist.add(bitmap);
        		}else{
        			sNoMemCacheURL.add(sURLList.get(i));
        		}
        	}
        }

        /*所有的图片都在内存中*/
        if(sNoMemCacheURL.size() == 0)
        {
        	BaseBlockImageShow tmpBaseBlockImageShow = new BaseBlockImageShow(mContext);
        	imageView.setImageDrawable(tmpBaseBlockImageShow.getImageViewBitmapLayer(sBitmaplist));
        	Log.d(TAG, "Memory cache to show bitmaps...");
        	
        }else if(cancelPotentialWork(sURLList, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);

            // NOTE: This uses a custom version of AsyncTask that has been pulled from the
            // framework and slightly modified. Refer to the docs at the top of the class
            // for more info on what was changed.
            task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, sNoMemCacheURL,sBitmaplist);
        }
    }
    
    public void loadImage(String aURL, ImageView imageView) {
        if (aURL == null) {
            return;
        }
        
        List<String> sURLList = new ArrayList<String>();//一个Imageview终于需要的图片（分很多层）
        sURLList.add(aURL);
        List<Bitmap> sBitmaplist = new ArrayList<Bitmap>();//记录所有URL中的Bitmap列表

        Bitmap bitmap = null;

        if (mImageCache != null) {
            bitmap = mImageCache.getBitmapFromMemCache(aURL);
        }

        if (bitmap != null) {
            // Bitmap found in memory cache
            imageView.setImageBitmap(bitmap);
        } else if (cancelPotentialWork(sURLList, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(mResources, mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);

            // NOTE: This uses a custom version of AsyncTask that has been pulled from the
            // framework and slightly modified. Refer to the docs at the top of the class
            // for more info on what was changed.
            task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, sURLList,sBitmaplist);
        }
    }
    
    /**
     * Set placeholder bitmap that shows when the the background thread is running.
     *
     * @param bitmap
     */
    public void setLoadingImage(Bitmap bitmap) {
        mLoadingBitmap = bitmap;
    }

    /**
     * Set placeholder bitmap that shows when the the background thread is running.
     *
     * @param resId
     */
    public void setLoadingImage(int resId) {
        mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
    }

    /**
     * Adds an {@link ImageCache} to this worker in the background (to prevent disk access on UI
     * thread).
     * @param fragmentManager
     * @param cacheParams
     */
    public void addImageCache(FragmentManager fragmentManager,ImageCache.ImageCacheParams cacheParams) 
    {
        mImageCacheParams = cacheParams;
        setImageCache(ImageCache.findOrCreateCache(fragmentManager, mImageCacheParams));
        new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
    }

    /**
     * Sets the {@link ImageCache} object to use with this ImageWorker. Usually you will not need
     * to call this directly, instead use {@link ImageWorker#addImageCache} which will create and
     * add the {@link ImageCache} object in a background thread (to ensure no disk access on the
     * main/UI thread).
     *
     * @param imageCache
     */
    public void setImageCache(ImageCache imageCache) {
        mImageCache = imageCache;
    }

    /**
     * If set to true, the image will fade-in once it has been loaded by the background thread.
     */
    public void setImageFadeIn(boolean fadeIn) {
        mFadeInBitmap = fadeIn;
    }

    public void setExitTasksEarly(boolean exitTasksEarly) {
        mExitTasksEarly = exitTasksEarly;
    }

    /**
     * Subclasses should override this to define any processing or work that must happen to produce
     * the final bitmap. This will be executed in a background thread and be long running. For
     * example, you could resize a large bitmap here, or pull down an image from the network.
     *
     * @param data The data to identify which image to process, as provided by
     *            {@link ImageWorker#loadImage(Object, ImageView)}
     * @return The processed bitmap
     */
    protected abstract Bitmap processBitmap(Object data);

    /**
     * Subclasses should override this to defind any process or work that must happen to produce
     * the final bitmap.you should resize a large bitmap 
     * @param data
     * @return
     */
    protected abstract Bitmap processLocalBitmap(String filePath);
    
    /**
     * Cancels any pending work attached to the provided ImageView.
     * @param imageView
     */
    public static void cancelWork(ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            bitmapWorkerTask.cancel(true);
            if (BuildConfig.DEBUG) {
                final Object bitmapData = bitmapWorkerTask.sURLList;
                Log.d(TAG, "cancelWork - cancelled work for " + bitmapData);
            }
        }
    }

    /**
     * Returns true if the current work has been canceled or if there was no work in
     * progress on this image view.
     * Returns false if the work in progress deals with the same data. The work is not
     * stopped in that case.
     */
    public static boolean cancelPotentialWork(List<String> sURLList, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final Object bitmapData = bitmapWorkerTask.sURLList;
            if (bitmapData == null || !sURLList.equals(bitmapData)) {
                bitmapWorkerTask.cancel(true);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "cancelPotentialWork - cancelled work for " + sURLList);
                }
            } else {
                // The same work is already in progress.
                return false;
            }
        }
        return true;
    }
    
    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active work task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * The actual AsyncTask that will asynchronously process the image.
     */
    private class BitmapWorkerTask extends AsyncTask<Object, Void, List<Bitmap>> {
        private List<String> sURLList;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Background processing.
         */
        @Override
        protected List<Bitmap> doInBackground(Object... params) {

            /*ImageView中所有层的图片URL中没有被下载的URL列表*/
            sURLList = (List<String>) params[0];

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "doInBackground - starting work" + sURLList + "****");
            }
            
            /*ImageView所有包含的每个层的Bitmap，有些Bitmap已经从Memory Cache中读取*/
            List<Bitmap> sBitmapList = (List<Bitmap>) params[1];
            
            /*异步网络获取各个图片的图片*/
            for(int i = 0 ;i < sURLList.size(); i++)
            {
	            final String sOneURL = sURLList.get(i);
	            Bitmap bitmap = null;
	
	            // Wait here if work is paused and the task is not cancelled
	            synchronized (mPauseWorkLock) {
	                while (mPauseWork && !isCancelled()) {
	                    try {
	                        mPauseWorkLock.wait();
	                    } catch (InterruptedException e) {}
	                }
	            }
	
	            // If the image cache is available and this task has not been cancelled by another
	            // thread and the ImageView that was originally bound to this task is still bound back
	            // to this task and our "exit early" flag is not set then try and fetch the bitmap from
	            // the cache
	            if (mImageCache != null 
	        		&& !isCancelled() 
	        		&& getAttachedImageView() != null
	                && !mExitTasksEarly) 
	            {
	                bitmap = mImageCache.getBitmapFromDiskCache(sOneURL);
	            }

	            // If the bitmap was not found in the cache and this task has not been cancelled by
	            // another thread and the ImageView that was originally bound to this task is still
	            // bound back to this task and our "exit early" flag is not set, then call the main
	            // process method (as implemented by a subclass).first process the local file
	            if (bitmap == null 
	        		&& !isCancelled() 
	        		&& getAttachedImageView() != null
	                && !mExitTasksEarly) 
	            {
	                bitmap = processLocalBitmap(sOneURL);
	            }
	            
	            // If the bitmap was not found in the cache and this task has not been cancelled by
	            // another thread and the ImageView that was originally bound to this task is still
	            // bound back to this task and our "exit early" flag is not set, then call the main
	            // process method (as implemented by a subclass)
	            if (bitmap == null 
	        		&& !isCancelled() 
	        		&& getAttachedImageView() != null
	                && !mExitTasksEarly) 
	            {
	                bitmap = processBitmap(sOneURL);
	            }
	
	            // If the bitmap was processed and the image cache is available, then add the processed
	            // bitmap to the cache for future use. Note we don't check if the task was cancelled
	            // here, if it was, and the thread is still running, we may as well add the processed
	            // bitmap to our cache as it might be used again in the future
	            if (bitmap != null && mImageCache != null) 
	            {
	                mImageCache.addBitmapToCache(sOneURL, bitmap);
	            }
	
	            if (BuildConfig.DEBUG) {
	                Log.d(TAG, "doInBackground - finished work");
	            }
	            
	            sBitmapList.add(bitmap);
            }
            return sBitmapList;
        }

        /**
         * Once the image is processed, associates it to the imageView
         */
        @Override
        protected void onPostExecute(List<Bitmap> aBitmapList) {
            // if cancel was called on this task or the "exit early" flag is set then we're done
            if (isCancelled() || mExitTasksEarly) {
            	aBitmapList = null;
            }

            final ImageView imageView = getAttachedImageView();
            if (aBitmapList != null && imageView != null) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onPostExecute - setting bitmap");
                }
                
                if(aBitmapList.size() == 1)
                {
                	setImageBitmap(imageView, aBitmapList.get(0)==null?null:new BitmapDrawable(aBitmapList.get(0)));
                }else{
                	BaseBlockImageShow tmpBaseBlockImageShow = new BaseBlockImageShow(mContext);
                	setImageBitmap(imageView, tmpBaseBlockImageShow.getImageViewBitmapLayer(aBitmapList));
                }
            }
        }

        @Override
        protected void onCancelled(List<Bitmap> aBitmapList) {
            super.onCancelled(aBitmapList);
            synchronized (mPauseWorkLock) {
                mPauseWorkLock.notifyAll();
            }
        }

        /**
         * Returns the ImageView associated with this task as long as the ImageView's task still
         * points to this task as well. Returns null otherwise.
         */
        private ImageView getAttachedImageView() {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask) {
                return imageView;
            }

            return null;
        }
    }

    
    /**
     * A custom Drawable that will be attached to the imageView while the work is in progress.
     * Contains a reference to the actual worker task, so that it can be stopped if a new binding is
     * required, and makes sure that only the last started worker process can bind its result,
     * independently of the finish order.
     */
    private static class AsyncDrawable extends BitmapDrawable 
    {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) 
        {
            super(res, bitmap);
            bitmapWorkerTaskReference =  new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() 
        {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     * Called when the processing is complete and the final bitmap should be set on the ImageView.
     *
     * @param imageView
     * @param bitmap
     */
    private void setImageBitmap(ImageView imageView, Drawable aDrawable) {
        if (mFadeInBitmap) {
            // Transition drawable with a transparent drwabale and the final bitmap
            final TransitionDrawable td = new TransitionDrawable(new Drawable[] {
									                            new ColorDrawable(android.R.color.transparent),
									                            aDrawable});
            // Set background to loading bitmap
            imageView.setBackgroundDrawable(new BitmapDrawable(mResources, mLoadingBitmap));
            imageView.setImageDrawable(td);
            td.startTransition(FADE_IN_TIME);
        } else {
        	
        	/*设置背景是为了防止aDrawable数据为空，例如本地图片解析失败，网络图片下载失败等*/
        	if (aDrawable == null) 
        	{
        		imageView.setImageBitmap(mLoadingBitmap);
			}else {
				imageView.setImageDrawable(aDrawable);
			}
        }
    }

    public void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            switch ((Integer)params[0]) {
                case MESSAGE_CLEAR:
                    clearCacheInternal();
                    break;
                case MESSAGE_INIT_DISK_CACHE:
                    initDiskCacheInternal();
                    break;
                case MESSAGE_FLUSH:
                    flushCacheInternal();
                    break;
                case MESSAGE_CLOSE:
                    closeCacheInternal();
                    break;
            }
            return null;
        }
    }

    protected void initDiskCacheInternal() {
        if (mImageCache != null) {
            mImageCache.initDiskCache();
        }
    }

    protected void clearCacheInternal() {
        if (mImageCache != null) {
            mImageCache.clearCache();
        }
    }

    protected void flushCacheInternal() {
        if (mImageCache != null) {
            mImageCache.flush();
        }
    }

    protected void closeCacheInternal() {
        if (mImageCache != null) {
            mImageCache.close();
            mImageCache = null;
        }
    }

    public void clearCache() {
        new CacheAsyncTask().execute(MESSAGE_CLEAR);
    }

    public void flushCache() {
        new CacheAsyncTask().execute(MESSAGE_FLUSH);
    }

    public void closeCache() {
        new CacheAsyncTask().execute(MESSAGE_CLOSE);
    }
}
