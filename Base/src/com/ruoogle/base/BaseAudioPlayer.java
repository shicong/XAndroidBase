package com.ruoogle.base;

import java.io.IOException;

import com.ruoogle.base.Util.Debug;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;

public class BaseAudioPlayer{
	
	private final String TAG = "AudioPlayer";
	private final boolean DEBUG = true;

	public interface IAudioPlayerCallBack{
		void audioPlayFinish(View aTrigView);//播放结束
		void audioPlayStop(View aTrigView);//播放被停止
	}
	
	private Context mContext;
	private MediaPlayer mMediaPlayer;
	private String mURL = "";//播放的音频的路径
	private IAudioPlayerCallBack mAudioPlayerCallBack;
	private View mTrigView;//触发播放事件的View
	
	public BaseAudioPlayer(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	/**
	 * 得到正在播放的URL
	 * @return
	 */
	public String getPlayingURL()
	{
		/*当前没有正在播放的URL*/
		if((mMediaPlayer == null) || (!mMediaPlayer.isPlaying()))
		{
			return "";
		}
		return mURL;
	}
	
	/**
	 * 判断是否在播放
	 * @author shicong
	 * */
	public boolean isPlaying()
	{
		if(mMediaPlayer != null)
		{
			return mMediaPlayer.isPlaying();
		}
		return false;
	}
	/**
	 * 得到Media Player
	 * @return
	 */
	private MediaPlayer getMediaPlayerInstance()
	{
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		/*设置数据准备就绪后的回调函数*/
		mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Debug.d(TAG, "onPrepared to start play", DEBUG);
				mp.start();
			}
		});
		
		/*设置结束后的回调函数*/
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Debug.d(TAG, "onCompletion play", DEBUG);
				
				mp.stop();
				mp.release();
				mMediaPlayer = null;
				mURL = "";
				
				if(mAudioPlayerCallBack != null)
				{
					mAudioPlayerCallBack.audioPlayFinish(mTrigView);
				}
			}
		});
		
		return mMediaPlayer;
	}
	
	/**
	 * 开始播放音频
	 * @param aURL
	 */
	public void startAudioPlay(String aURL,IAudioPlayerCallBack aCallBack,View aTrigView) {
		
		if(aURL == null)
		{
			return;
		}
		
		if(mMediaPlayer == null)
		{
			mMediaPlayer = getMediaPlayerInstance();
		}

		/*如果在播放则停止*/
		if(mMediaPlayer.isPlaying())
		{
			/*调用停止的回调*/
			mAudioPlayerCallBack.audioPlayStop(mTrigView);
			
			if (aURL.equals(mURL)) {
				Debug.d(TAG, "stop the current audio...", DEBUG);
				stopAudioPlay();
				return;
			}else {
				Debug.d(TAG, "reset to another audio...", DEBUG);
				mMediaPlayer.reset();
			}
		}
		
		/*播放时设置回调*/
		mAudioPlayerCallBack = aCallBack;
		mTrigView = aTrigView;
		
		/*播放音频*/
		try {
			mMediaPlayer.setDataSource(aURL);
			mMediaPlayer.prepareAsync();
			mURL = aURL;
			return;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			Debug.d(TAG, "audio url is wrong...", DEBUG);
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*出错后是否资源*/
		stopAudioPlay();
		return;
	}
	
	/**
	 * 停止播放
	 * @author shicong
	 */
	public void stopAudioPlay() {
		if((mMediaPlayer == null))
		{
			return;
		}
		
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = null;
		mURL = "";
		return;
	}
}
