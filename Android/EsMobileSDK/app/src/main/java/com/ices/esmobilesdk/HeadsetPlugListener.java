package com.ices.esmobilesdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.excelsecu.driver.audio.HeadsetPlug;

import static com.excelsecu.driver.audio.HeadsetPlug.*;

/**
 * Created by Lehyu on 2016/5/16.
 */
public class HeadsetPlugListener {
    public static interface OnHeadsetChangeListener {
        /**
         * 通知外部耳机是否插入
         *
         * @param headsetName
         * @param headsetInsert true表示耳机插入，否则表示移除
         * @param micInsert true表示话筒插入，否则表示移除
         */
        void onHeadsetChange(String headsetName, boolean headsetInsert, boolean micInsert);
    }

    public class HeadsetPlugReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPlugListener != null) {
                boolean headsetInsert = (intent.getIntExtra("state", 0) != 0);
                boolean micInsert = (intent.getIntExtra("microphone", 0) != 0);
                String str = intent.getStringExtra("name");
                if (null == str) {
                    str = "";
                }

                mPlugListener.onHeadsetChange(str, headsetInsert, micInsert);
            }
        }
    }

    private OnHeadsetChangeListener mPlugListener = null;
    private HeadsetPlugReceiver mHeadsetReceiver = null;
    private Context mContext = null;

    public HeadsetPlugListener() {

    }

    /**
     * 初始化并注册我们的receiver
     *
     * @param context
     * @return
     */
    public void registerReceiver(Context context) {
        if ((null == context) || (mHeadsetReceiver != null)) {
            return;
        }
        mHeadsetReceiver = new HeadsetPlugReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        mContext = context.getApplicationContext();
        mContext.registerReceiver(mHeadsetReceiver, intentFilter);
    }

    /**
     * 注销我们的receiver
     *
     * @param
     * @return
     */
    public void unregisterReceiver() {
        if ((mContext != null) && (mHeadsetReceiver != null)) {
            mContext.unregisterReceiver(mHeadsetReceiver);
            mHeadsetReceiver = null;
        }
    }

    /**
     * 设置外部的监听器
     *
     * @param onChangeListener
     */
    public void setOnHeadsetChangeListener(OnHeadsetChangeListener onChangeListener) {
        mPlugListener = onChangeListener;
    }
}
