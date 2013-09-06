package com.ruoogle.base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ruoogle.base.Util.Debug;
import com.ruoogle.base.Util.UtilSystem;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 录音和音量监控模块
 * @author shicong
 */
public class BaseAudioRecorder {

	/**
	 * 录音结束后的回调接口
	 * @author shicong
	 */
	public interface IRecordStopCallBack{
		void RecordCallBack(long aRecordDurationTime);
	}
	
	
	private final String TAG = "AudioRecord";
	private final boolean DEBUG = true;
	
	private int mRecord_Max_Time = 60*1000;//
	
	private Context mContext;
	private ViewGroup mViewGroupMonitorRootView;//根View
	private Button mBtnTrig;//录音的触发按钮
	private File mAudioFile;//录音文件
	
	/*通过Media Recorder录音*/
	private MediaPlayer   mPlayer = null;

	/*录音监控*/
	private boolean isMonitorRun = false;
	private VolumeMonitor mVolumeMonitorTask;
	
	/*录音结束后的回调函数*/
	private IRecordStopCallBack mCallBack;
	
	public BaseAudioRecorder(Context context,ViewGroup aMonitorRootView,Button aTrigButton)
	{
		mContext = context;
		mViewGroupMonitorRootView = aMonitorRootView;
		mBtnTrig = aTrigButton;
		initViews();
	}
	
	/**
	 * 初始化View
	 * @author shicong
	 */
	private void initViews()
	{
		mBtnTrig.setOnLongClickListener(mRecordOnLongClickListener);
		mBtnTrig.setOnClickListener(mRecordOnClickListener);
		mBtnTrig.setOnTouchListener(mRecordOnTouchListener);
	}
	
	/**
	 * @author shicong
	 * @param aTime:最大的持续时间
	 */
	public void setRecordMaxTime(int aTime)
	{
		mRecord_Max_Time = aTime;
	}

	/**
	 * 设置回放的按钮
	 * @author shicong
	 * @param aButton
	 */
	public void setPlayBackButton(Button aButton)
	{
		aButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mAudioFile != null)
				{
					if((mPlayer != null) && mPlayer.isPlaying())
					{
						stopPlaying();
					}else{
						startPlaying(mAudioFile);
					}
				}
			}
		});
	}
	
	/**
	 * 设置录音结束的回调函数
	 * @author shicong
	 * @param aCallBack
	 */
	public void setRecordCallBack(IRecordStopCallBack aCallBack)
	{
		mCallBack = aCallBack;
	}
	
	/**
	 * 返回得到录音后的文件
	 * @return
	 */
	public File getRecordFile()
	{
		return mAudioFile;
	}
	
	
	/**
	 * 录音按钮的长按和短按功能
	 * @author admin
	 * */
	private OnLongClickListener mRecordOnLongClickListener = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			Debug.d(TAG, "onLongClick start to record...", DEBUG);
			
			/*重新录音时，删除已有的录音文件*/
			if(mAudioFile != null)
			{
				mAudioFile.delete();
			}
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			mAudioFile = new File(Environment.getExternalStorageDirectory()+"/Phamily",timeStamp + ".amr");
	 			
			startRecording(mAudioFile);
			
			return true;
		}
	};
	
	/**
	 * 按键提示时间太短
	 * */
	private OnClickListener mRecordOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Debug.d(TAG, "onClick start to record but time is so short...", DEBUG);
			UtilSystem.SystemToast(mContext, "时间太短了哦~~~");
		}
	};
	
	/**
	 * 触屏事件用于停止录音
	 * @author shicong
	 * */
	OnTouchListener mRecordOnTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch(event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					Debug.d(TAG, "ACTION_DOWN", DEBUG);
					break;
				case MotionEvent.ACTION_UP:
					Debug.d(TAG, "ACTION_UP stop record", DEBUG);
					stopRecording();
					break;
			}
			
			return false;
		}
	};	

	/**
	 * 录音
	 * @author shicong
	 * */
    private void startRecording(File aFile) {
    	MediaRecorder sRecorder = new MediaRecorder();
    	sRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    	sRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    	sRecorder.setOutputFile(aFile.getAbsolutePath());
    	sRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    	sRecorder.setMaxDuration(mRecord_Max_Time);
        try {
        	sRecorder.prepare();
        } catch (IOException e) {
            Debug.d(TAG, "Start record error...", DEBUG);
            UtilSystem.SystemToast(mContext, "录音失败！~~");
            return;
        }
        
        /*数据监控*/
		mVolumeMonitorTask = new VolumeMonitor();
		mVolumeMonitorTask.execute(sRecorder);
    }

    /**
     * 停止录音
     * @author shicong
     * */
    private void stopRecording() {
		/*停止监控*/
		isMonitorRun = false;
    }
    
    /**
     * 播放录音
     * @author shicong
     * */
    private void startPlaying(File aFile) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(aFile.getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Debug.d(TAG, "prepare() failed",DEBUG);
        }
    }

    /**
     * 停止播放录音
     * @author shicong
     * */
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }	

    
	/**
	 * 异步监视音量
	 * @author shicong
	 * */
	private class VolumeMonitor extends AsyncTask<MediaRecorder, Void, Void>{

		private TextView mViewMonitorTime;
		private TextView mViewVolume;
		private int mVolumeSize;
		private Handler mHandler;
		private long mRecordDurationTime = 0;
		private CountDownTimer mCountDownTimer;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			/*音量监控的弹出框*/
			mViewMonitorTime = (TextView) mViewGroupMonitorRootView.findViewById(R.id.ID_AUDIO_MONITOR_TIME);
			mViewVolume = (TextView) mViewGroupMonitorRootView.findViewById(R.id.ID_AUDIO_MONITOR_VOLUME);
			mViewGroupMonitorRootView.setVisibility(View.VISIBLE);
			mHandler = new Handler();
			
			/*开始录音计时*/
			mRecordDurationTime = 0;
			mCountDownTimer = new CountDownTimer(mRecord_Max_Time, 1000) {

	    	     public void onTick(long millisUntilFinished) {
	    	    	 mRecordDurationTime = (mRecord_Max_Time - millisUntilFinished)/1000 + 1;
	    	    	 mViewMonitorTime.setText(mRecordDurationTime + "\"");
	    	     }

	    	     public void onFinish() {
	    	    	 stopRecording();
	    	     }
	    	  }.start();    	
	 			
		}
		
		@Override
		protected Void doInBackground(MediaRecorder... params) {
			
			/*开始录音*/
			MediaRecorder tmpMediaRecorder = params[0];
			tmpMediaRecorder.start();
			tmpMediaRecorder.setOnInfoListener(new OnInfoListener() {
				@Override
				public void onInfo(MediaRecorder mr, int what, int extra) {
					// TODO Auto-generated method stub
					if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED)
					{
						isMonitorRun = false;
					}
				}
			});
			
			/*开始监控*/
			isMonitorRun = true;
			while(isMonitorRun)
			{
				int sVolumeValue = tmpMediaRecorder.getMaxAmplitude();
				if( sVolumeValue > 0)
				{
					mVolumeSize = 1000 * sVolumeValue / 32768;
					mHandler.postAtTime(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							mViewVolume.setText(mVolumeSize+"");
						}
					},500);
				}
			}
			
			/*停止录音*/
			tmpMediaRecorder.stop();
			tmpMediaRecorder.release();
			tmpMediaRecorder = null;
			mCountDownTimer.cancel();//停止计时
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mViewGroupMonitorRootView.setVisibility(View.GONE);
			
	        /*停止时计算录音时间*/
			if(mCallBack != null)
			{
				mCallBack.RecordCallBack(mRecordDurationTime);
			}
		}
	}    
  	
}
