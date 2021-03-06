package me.khrystal.view.transferee.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.File;

/**
 * usage: 使用 <a href="https://github.com/nostra13/Android-Universal-Image-Loader">
 * Android-Universal-Image-Loader</a>作为 Transferee 的图片加载器
 * author: kHRYSTAL
 * create time: 18/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class UniversalImageLoader implements ImageLoader {

    private Context context;
    private DisplayImageOptions normalImageOptions;

    private UniversalImageLoader(Context context) {
        this.context = context;
        initImageLoader(context);
    }

    public static UniversalImageLoader with(Context context) {
        return new UniversalImageLoader(context);
    }

    private void initImageLoader(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCache memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        normalImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(normalImageOptions)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(memoryCache)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);
    }

    @Override
    public void showImage(String imageUrl, ImageView imageView, Drawable placeholder, final SourceCallback sourceCallback) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeholder)
                .showImageOnFail(placeholder)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(imageUrl, imageView,
                options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        if (sourceCallback != null) {
                            sourceCallback.onStart();
                        }
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        if (sourceCallback != null) {
                            sourceCallback.onDelivered(STATUS_DISPLAY_FAILED);
                        }
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        if (sourceCallback != null) {
                            sourceCallback.onFinish();
                            sourceCallback.onDelivered(STATUS_DISPLAY_SUCCESS);
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        if (sourceCallback != null) {
                            sourceCallback.onDelivered(STATUS_DISPLAY_CANCEL);
                        }
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        if (sourceCallback != null) {
                            sourceCallback.onProgress(current * 100 / total);
                        }
                    }
                });
    }

    @Override
    public void loadImageAsync(String imageUrl, final ThumbnailCallback callback) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(imageUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                callback.onFinsih(null);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                callback.onFinsih(new BitmapDrawable(context.getResources(), bitmap));
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                callback.onFinsih(null);
            }
        });
    }

    @Override
    public Drawable loadImageSync(String imageUrl) {
        Bitmap bitmap = com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(imageUrl, normalImageOptions);
        return new BitmapDrawable(bitmap);
    }

    @Override
    public boolean isLoaded(String url) {
        File cache = com.nostra13.universalimageloader.core.ImageLoader.getInstance().getDiskCache().get(url);
        return cache != null && cache.exists();
    }

    @Override
    public void clearCache() {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().getMemoryCache().clear();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().getDiskCache().clear();
    }
}
