package cn.edu.xidian.sast.baselib.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by ShiningForever on 2017/7/8.
 */

public class BaseImageLoaderTest {
    private Context mContext;
    private ImageView imageView;

    private void testApi() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(mContext).build();

        DisplayImageOptions options = new DisplayImageOptions.Builder().build();
        imageLoader.displayImage("url", imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                super.onLoadingCancelled(imageUri, view);
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
            }
        });
    }

    private void myLoader() {
//        ImageLoaderManager.getInstance(context).displayImage();
    }
}
