package cn.edu.xidian.sast.baselib.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by ShiningForever on 2017/7/8.
 *
 */

public class ImageLoaderManager {
    private static final String TAG = "ImageLoaderManager";

    // UniversalImageLoader最多线程数
    private static final int THREAD_COUNT = 4;
    // 图片加载优先级（一般低于文本优先级）
    private static final int PRIORITY = 2;
    // 硬盘缓存大小
    private static final int DISK_CACHE_SIZE = 50 * 1024;
    // 超时
    private static final int CONNECTION_TIME_OUT = 5 * 1000;
    private static final int READ_TIME_OUT = 30 * 1000;

    private static ImageLoaderManager mManager = null;
    private static ImageLoader mLoader = null;

    // 构造方法中完成对ImageLoader的配置和实例化
    private ImageLoaderManager(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_COUNT)
                .threadPriority(Thread.NORM_PRIORITY - PRIORITY) // 防止不同系统对优先级数字的定义不同
                .denyCacheImageMultipleSizesInMemory() // 防止缓存多套尺寸
                .memoryCache(new WeakMemoryCache()) // 弱引用缓存
                .diskCacheSize(DISK_CACHE_SIZE)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 用MD5为缓存文件命名
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(getDefaultOptions()) // 图片加载配置
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
//                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(configuration); // 利用configuration配置ImageLoader
        mLoader = ImageLoader.getInstance();
    }

    private DisplayImageOptions getDefaultOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.uilsdk_img_error)
//                .showImageOnFail(R.drawable.uilsdk_img_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .decodingOptions(new BitmapFactory.Options())
                .build();
        return options;
    }

    public static ImageLoaderManager getInstance(Context context) {
        if (mManager == null) {
            synchronized (ImageLoaderManager.class) {
                if (mManager == null) {
                    mManager = new ImageLoaderManager(context);
                }
            }
        }
        return mManager;
    }

    // 把原始ImageLoader的displayImage方法封装到本类ImageLoaderManager中
    private void displayImage(String url, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {

        Log.d(TAG, "URL:" + url + " ImageView:" + imageView);
        if (mLoader != null) {
            mLoader.displayImage(url, imageView, options, listener);
        }
    }

    public void displayImage(String url, ImageView imageView, ImageLoadingListener listener) {
        displayImage(url, imageView, null, listener);
    }

    public void displayImage(String url, ImageView imageView) {
        displayImage(url, imageView, null, null);
    }
}
