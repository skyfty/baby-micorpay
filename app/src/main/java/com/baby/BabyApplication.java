package com.baby;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.unionpay.cloudpos.POSTerminal;
import com.unionpay.cloudpos.msr.MSRDevice;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class BabyApplication extends Application {
	private static BabyApplication mInstance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		new File(GlobalParam.DOWNLOAD_DIR).mkdirs();
		initImageLoader();

	}

	public void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.diskCacheFileCount(10000)// 缓存文件的最大个数
				.tasksProcessingOrder(QueueProcessingType.FIFO)// 设置图片下载和显示的工作队列排序
				.memoryCache(new WeakMemoryCache()).threadPoolSize(3).build();
		ImageLoader.getInstance().init(config);
	}


	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) mInstance.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
		Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = i.next();
			try {
				if (info.pid == pID) {
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

}